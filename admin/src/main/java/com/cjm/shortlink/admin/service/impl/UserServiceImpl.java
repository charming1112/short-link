package com.cjm.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjm.shortlink.admin.common.convention.exception.ClientException;
import com.cjm.shortlink.admin.common.convention.exception.ServiceException;
import com.cjm.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.cjm.shortlink.admin.config.RBloomFilterConfiguration;
import com.cjm.shortlink.admin.dao.entity.UserDO;
import com.cjm.shortlink.admin.dao.mapper.UserMapper;
import com.cjm.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cjm.shortlink.admin.dto.resp.UserRespDTO;
import com.cjm.shortlink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.cjm.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.cjm.shortlink.admin.common.enums.UserErrorCodeEnum.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserDO> implements UserService {


    @Autowired
    RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public UserRespDTO getUserByUsername(String username) {

        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);

        UserDO userDO = baseMapper.selectOne(wrapper);
        if (userDO == null) {
            throw new ServiceException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO,result);
        return result;
    }

    /**
     * 根据用户名判断用户是否存在(判断用户名是否可用)
     * @param username
     * @return
     */
    @Override
    public Boolean hasUserName(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    /**
     * 注册用户
     * @param requestParam
     */
    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if(!hasUserName(requestParam.getUsername())) {
            throw new ClientException(USER_NAME_EXIST);
        }

        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY+ requestParam.getUsername());

        try{
            if (lock.tryLock()){
                UserDO userDO = BeanUtil.toBean(requestParam, UserDO.class);
                int insert = baseMapper.insert(userDO);
                if (insert<1){
                    throw new ClientException(USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(userDO.getUsername());
            }else {
                throw new ClientException(USER_NAME_EXIST);
            }
        }finally {
            lock.unlock();
        }

    }
}
