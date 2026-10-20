package com.jsu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsu.mapper.EmpExprMapper;
import com.jsu.mapper.EmpMapper;
import com.jsu.pojo.*;
import com.jsu.service.EmpService;
import com.jsu.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    /**
     * 分页查询员工
     *
     * @param empQueryParam 查询条件
     * @return 分页查询结果
     */
    @Override
    public PageResult page(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> empList = empMapper.list(empQueryParam);
        PageInfo<Emp> pageInfo = new PageInfo<>(empList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加员工
     *
     * @param emp 员工信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        // 1. 检查用户名是否已存在
        Emp existingEmp = empMapper.getByUsername(emp.getUsername());
        if (existingEmp != null) {
            throw new RuntimeException("用户名已被占用");
        }

        // 2. 补全基础属性
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        // 3. 保存员工基本信息
        empMapper.insert(emp);

        // 3. 保存员工的工作经历信息 - 批量
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            // 设置员工ID
            for (EmpExpr empExpr : exprList) {
                empExpr.setEmpId(empId);
            }
            // 批量插入工作经历
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * 批量删除员工
     *
     * @param ids 员工id列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Integer> ids) {
        // 1. 先根据员工的ID批量删除员工的工作经历信息（子表）
        empExprMapper.deleteByEmpIds(ids);

        // 2. 再根据ID批量删除员工基本信息（主表）
        empMapper.deleteByIds(ids);
    }

    /**
     * 查询回显
     *
     * @param id 员工id
     * @return 员工信息
     */
    @Override
    public Emp getInfo(Integer id) {
        Emp emp = empMapper.getById(id);
        if (emp != null) {
            emp.setPassword(null); // 编辑回显时不要返回密码
        }
        return emp;
    }

    /**
     * 修改员工信息
     *
     * @param emp 员工信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        // 1. 根据ID更新员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        // 2. 根据员工ID删除员工的工作经历信息
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        // 3. 新增员工的工作经历数据
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> empExpr.setEmpId(empId));
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * 查询全部员工
     *
     * @return 员工列表
     */
    @Override
    public List<Emp> findAll() {
        return empMapper.findAll();
    }

    /**
     * 员工登录
     *
     * @param emp 员工信息
     * @return 员工信息
     */
    @Override
    public LoginInfo login(Emp emp) {
        Emp empLogin = empMapper.getUsernameAndPassword(emp);
        if (empLogin != null) {
            // 生成JWT令牌
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", empLogin.getId());
            dataMap.put("username", empLogin.getUsername());
            dataMap.put("name", empLogin.getName());
            dataMap.put("role", empLogin.getRole());

            String jwt = JwtUtils.generateJwt(dataMap);
            LoginInfo loginInfo = new LoginInfo(empLogin.getId(), empLogin.getUsername(), empLogin.getName(),
                    empLogin.getRole(), jwt);
            return loginInfo;
        }
        return null;
    }
}
