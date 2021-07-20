package com.zjut.student_outside.controller;

import com.zjut.student_outside.model.User;
import com.zjut.student_outside.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sys")
public class UserController
{
    @Autowired
    private UserService userSer;

    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestBody User user)
    {
        if (userSer.login(user).isPresent()) {
            return userSer.login(user).orElseThrow();
        } else {
            return "账号或密码错误";
        }
    }

    @PostMapping("/updateInfo")
    @ResponseBody
    public Object updateInfo(@RequestBody User user)
    {
        User afterUser = (User) userSer.update(user);
        if (afterUser != null) {
            return "OK";
        }
        return "fail";
    }
}
