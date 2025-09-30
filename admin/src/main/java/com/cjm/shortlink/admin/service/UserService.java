package com.cjm.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjm.shortlink.admin.dao.entity.UserDO;
import com.cjm.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cjm.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户服务类
 */
public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);

    Boolean hasUserName(String username);

    void register(UserRegisterReqDTO requestParam);
}
