package com.jsu.service.Impl;

import com.jsu.mapper.DeptMapper;
import com.jsu.pojo.Dept;
import com.jsu.pojo.DeptDeleteException;
import com.jsu.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        // 检查该部门下是否有员工
        int count = deptMapper.countEmpByDeptId(id);
        if (count > 0) {
            // 如果部门下有员工，抛出异常，不允许删除
            throw new DeptDeleteException();
        }
        // 如果部门下没有员工，执行删除操作
        deptMapper.deleteById(id);
    }

    @Override
    public void add(Dept dept) {
        //1.补全基础属性 updateTime createTime
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        //2.调用Mapper接口方法插入数据
        deptMapper.insert(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }
}
