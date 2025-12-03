package com.cjm.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cjm.shortlink.admin.common.biz.user.UserContext;
import com.cjm.shortlink.admin.common.convention.exception.ServiceException;
import com.cjm.shortlink.admin.common.convention.result.Result;
import com.cjm.shortlink.admin.dao.entity.GroupDO;
import com.cjm.shortlink.admin.dao.mapper.GroupMapper;
import com.cjm.shortlink.admin.remote.ShortLinkRemoteService;
import com.cjm.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.cjm.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.cjm.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };


    private final GroupMapper groupMapper;
    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);

        List<GroupDO> groupDOList = groupMapper.selectList(wrapper);
        if (CollUtil.isEmpty(groupDOList)){
            throw new ServiceException("该用户无分组");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return shortLinkRemoteService.pageRecycleBinShortLink(requestParam);
    }
}
