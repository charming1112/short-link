package com.cjm.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cjm.shortlink.admin.common.convention.result.Result;
import com.cjm.shortlink.admin.common.convention.result.Results;
import com.cjm.shortlink.admin.dto.req.UserLoginReqDTO;
import com.cjm.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cjm.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.cjm.shortlink.admin.dto.resp.UserActualRespDTO;
import com.cjm.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.cjm.shortlink.admin.dto.resp.UserRespDTO;
import com.cjm.shortlink.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 通过用户名获取无脱敏用户
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUserName(@PathVariable("username") String username){
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }

    /**
     * 判断用户名是否可用（用户名是否存在）
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUserName(@RequestParam("username") String username){
        return Results.success(userService.hasUserName(username));
    }

    /**
     * 用户注册
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam ){

        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO userUpdateReqDTO){

        userService.update(userUpdateReqDTO);
        return Results.success();
    }

    /**
     * 用户登录
     * @param userLoginReqDTO
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO userLoginReqDTO){
        return Results.success(userService.login(userLoginReqDTO)) ;
    }

    /**
     * 检查是否登录成功
     * @param token
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam String token,@RequestParam String username){
        return Results.success(userService.checkLogin(token,username));
    }

    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void> logout(@RequestParam String token, @RequestParam String username){
        userService.logout(token,username);
        return Results.success();
    }

}
