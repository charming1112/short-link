package com.cjm.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjm.shortlink.project.common.convention.exception.ServiceException;

import com.cjm.shortlink.project.dao.entity.ShortLinkDO;
import com.cjm.shortlink.project.dao.mapper.ShortLinkMapper;
import com.cjm.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.cjm.shortlink.project.service.ShortLinkService;
import com.cjm.shortlink.project.toolkit.HashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {


    @Autowired
    private  RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;;

    @Override
    public ShortLinkCreateRespDTO creatShortLink(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {

        String shortLinkSuffix = generateSuffix(shortLinkCreateReqDTO);
        String fullShortUrl= shortLinkCreateReqDTO.getDomain()+"/"+shortLinkSuffix;

        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(shortLinkCreateReqDTO.getDomain())
                .originUrl(shortLinkCreateReqDTO.getOriginUrl())
                .gid(shortLinkCreateReqDTO.getGid())
                .createdType(shortLinkCreateReqDTO.getCreatedType())
                .validDateType(shortLinkCreateReqDTO.getValidDateType())
                .validDate(shortLinkCreateReqDTO.getValidDate())
                .describe(shortLinkCreateReqDTO.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortUrl)
                .build();

        try {
            baseMapper.insert(shortLinkDO);

        }catch (DuplicateKeyException ex){

            LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl);

            ShortLinkDO hasShortLinkDo = baseMapper.selectOne(queryWrapper);

            if (hasShortLinkDo!=null){
                throw new ServiceException(String.format("短链接：%s 生成重复", fullShortUrl));
            }
        }


        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .originUrl(shortLinkDO.getOriginUrl())
                .gid(shortLinkDO.getGid())
                .build();

    }

    private String generateSuffix(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {

        int customGenerateCount=0;//重试次数

        String shortUri;

        while(true){

            if (customGenerateCount>10){
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }
             shortUri = HashUtil.hashToBase62(shortLinkCreateReqDTO.getOriginUrl()+System.currentTimeMillis());
            if (!shortUriCreateCachePenetrationBloomFilter.contains(shortLinkCreateReqDTO.getDomain()+"/"+shortUri)){
                break;
            }
            customGenerateCount++;
        }

        return shortUri;

    }
}
