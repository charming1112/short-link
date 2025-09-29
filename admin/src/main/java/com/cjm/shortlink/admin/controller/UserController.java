package com.cjm.shortlink.admin.controller;

import com.cjm.shortlink.admin.common.convention.result.Result;
import com.cjm.shortlink.admin.common.convention.result.Results;
import com.cjm.shortlink.admin.dto.resp.UserRespDTO;
import com.cjm.shortlink.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */

    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        return Results.success(userService.getUserByUsername(username));
    }
}
