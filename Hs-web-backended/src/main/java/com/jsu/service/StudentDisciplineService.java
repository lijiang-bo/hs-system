package com.jsu.service;

import com.jsu.pojo.PageResult;
import com.jsu.pojo.StudentDisciplineQueryParam;

public interface StudentDisciplineService {
    PageResult page(StudentDisciplineQueryParam studentDisciplineQueryParam);
}
