package com.cjm.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjm.shortlink.project.dao.entity.ShortLinkDO;
import com.cjm.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

public interface ShortLinkService extends IService<ShortLinkDO> {
    ShortLinkCreateRespDTO creatShortLink(ShortLinkCreateReqDTO shortLinkCreateReqDTO);
}
