package com.jsu.mapper;

import com.jsu.pojo.StudentDiscipline;
import com.jsu.pojo.StudentDisciplineQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentDisciplineMapper {
    List<StudentDiscipline> page(StudentDisciplineQueryParam studentDisciplineQueryParam);
}
