package com.cjm.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjm.shortlink.project.dao.entity.ShortLinkDO;
import com.cjm.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkPageRespDTO;

public interface RecycleBinService extends IService<ShortLinkDO> {
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);

    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
