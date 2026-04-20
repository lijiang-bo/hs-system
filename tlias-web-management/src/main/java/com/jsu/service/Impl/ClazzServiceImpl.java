package com.jsu.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jsu.mapper.ClazzMapper;
import com.jsu.pojo.Clazz;
import com.jsu.pojo.ClazzDeleteException;
import com.jsu.pojo.ClazzQueryParam;
import com.jsu.pojo.PageResult;
import com.jsu.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    /**
     * 分页查询班级
     *
     * @param clazzQueryParam 查询条件
     * @return 分页查询结果
     */
    @Override
    public PageResult page(ClazzQueryParam clazzQueryParam) {
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());

        List<Clazz> clazzList = clazzMapper.page(clazzQueryParam);

        Page<Clazz> p = (Page<Clazz>) clazzList;
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 新增班级信息
     *
     * @param clazz 班级信息
     */
    @Override
    public void save(Clazz clazz) {
        clazzMapper.insertClazz(clazz);
    }


    /**
     * 根据ID查询班级
     *
     * @param id 班级ID
     * @return 班级信息
     */
    @Override
    public Clazz getById(Long id) {
        return clazzMapper.selectById(id);
    }

    /**
     * 更新班级信息
     *
     * @param clazz 班级信息
     */
    @Override
    public void update(Clazz clazz) {
        clazzMapper.updateClazz(clazz);
    }

    /**
     * 删除班级
     *
     * @param id 班级ID
     */
    @Override
    public void delete(Long id) {
        // 检查班级下是否有学生
        Integer studentCount = clazzMapper.countStudentByClazzId(id);
        if (studentCount > 0) {
            // 如果班级下有学生，抛出异常
            throw new ClazzDeleteException();
        }
        // 删除班级
        clazzMapper.deleteById(id);
    }

    @Override
    public List<Clazz> findAll() {
        return clazzMapper.findAll();
    }

}
