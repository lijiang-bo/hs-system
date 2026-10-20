package com.jsu.controller;

import com.jsu.pojo.Emp;
import com.jsu.pojo.LoginInfo;
import com.jsu.pojo.Result;
import com.jsu.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("员工来登录啦 , 用户名: {}", emp.getUsername());
        // 参数校验
        if (emp.getUsername() == null || emp.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (emp.getPassword() == null || emp.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }
        LoginInfo loginInfo = empService.login(emp);
        if (loginInfo != null) {
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误~");
    }

}
