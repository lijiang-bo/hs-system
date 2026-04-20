package com.jsu.mapper;


import com.jsu.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工工作经历Mapper接口
 */
@Mapper
public interface EmpExprMapper {

    /**
     * 批量插入员工工作经历信息
     *
     * @param exprList 员工工作经历信息列表
     */
    void insertBatch(@Param("exprList") List<EmpExpr> exprList);

    /**
     * 根据员工id列表批量删除员工工作经历信息
     *
     * @param empIds 员工id列表
     */
    void deleteByEmpIds(List<Integer> empIds);
}
