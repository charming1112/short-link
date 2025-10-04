package com.cjm.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjm.shortlink.admin.dao.entity.UserDO;
import com.cjm.shortlink.admin.dto.req.UserLoginReqDTO;
import com.cjm.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cjm.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.cjm.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.cjm.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户服务类
 */
public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);

    Boolean hasUserName(String username);

    void register(UserRegisterReqDTO requestParam);

    void update(UserUpdateReqDTO userUpdateReqDTO);

    UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO);

    Boolean checkLogin(String token, String username);

    void logout(String token, String username);
}
