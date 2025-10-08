package com.cjm.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjm.shortlink.admin.common.convention.exception.ClientException;
import com.cjm.shortlink.admin.common.convention.exception.ServiceException;
import com.cjm.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.cjm.shortlink.admin.dao.entity.UserDO;
import com.cjm.shortlink.admin.dao.mapper.UserMapper;
import com.cjm.shortlink.admin.dto.req.UserLoginReqDTO;
import com.cjm.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.cjm.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.cjm.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.cjm.shortlink.admin.dto.resp.UserRespDTO;
import com.cjm.shortlink.admin.service.GroupService;
import com.cjm.shortlink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cjm.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.cjm.shortlink.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static com.cjm.shortlink.admin.common.enums.UserErrorCodeEnum.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserDO> implements UserService {


    @Autowired
    private RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GroupService groupService;


    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
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
                groupService.saveGroup(requestParam.getUsername(),"默认分组");
            }else {
                throw new ClientException(USER_NAME_EXIST);
            }
        }finally {
            lock.unlock();
        }

    }

    /**
     * 修改用户信息
     * @param userUpdateReqDTO
     */
    @Override
    public void update(UserUpdateReqDTO userUpdateReqDTO) {

        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, userUpdateReqDTO.getUsername());
        baseMapper.update(BeanUtil.toBean(userUpdateReqDTO, UserDO.class),wrapper);

    }

    /**
     * 登录
     * @param userLoginReqDTO
     * @return
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO) {

        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, userLoginReqDTO.getUsername())
                .eq(UserDO::getPassword, userLoginReqDTO.getPassword());

        UserDO userDO = baseMapper.selectOne(wrapper);

        if (userDO==null){
            throw new ClientException(USER_NULL);
        }
        //获取token
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + userLoginReqDTO.getUsername());

        if (CollUtil.isNotEmpty(hasLoginMap)){
            //延长过期时间
            stringRedisTemplate.expire(USER_LOGIN_KEY + userLoginReqDTO.getUsername(), 30L, TimeUnit.MINUTES);

            String token = hasLoginMap.keySet().stream()
                    .findFirst()  // 获取第一个key（假设key是token）
                    .map(Object::toString)  // 转换为字符串
                    .orElseThrow(() -> new ClientException("用户登录错误"));  // 无token时抛异常
            return new UserLoginRespDTO(token);
        }


        /**
         * 如果Redis中不存在token，创建一个uuid作为token，并且存入Redis
         */
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY+userLoginReqDTO.getUsername(),uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(USER_LOGIN_KEY + userLoginReqDTO.getUsername(), 30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(uuid);

    }

    /**
     * 检查是否登录成功
     * @param token
     * @param username
     * @return
     */
    @Override
    public Boolean checkLogin(String token, String username) {

        return stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token) != null;

    }

    /**
     * 退出登录
     * @param token
     * @param username
     */
    @Override
    public void logout(String token, String username) {

        if (!checkLogin(token,username)){
            throw new ClientException("用户Token不存在或用户未登录");
        }

        stringRedisTemplate.delete(USER_LOGIN_KEY+username);
    }
}
