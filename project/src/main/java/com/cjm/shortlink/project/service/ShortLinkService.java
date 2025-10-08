package com.cjm.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjm.shortlink.project.dao.entity.ShortLinkDO;
import com.cjm.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.util.List;

public interface ShortLinkService extends IService<ShortLinkDO> {
    ShortLinkCreateRespDTO creatShortLink(ShortLinkCreateReqDTO shortLinkCreateReqDTO);

    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO shortLinkPageReqDTO);

    List<ShortLinkGroupCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam);

    void updateShortLink(ShortLinkUpdateReqDTO shortLinkUpdateReqDTO);

    void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) throws IOException;
}
