package com.cjm.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjm.shortlink.admin.common.convention.result.Results;
import com.cjm.shortlink.admin.remote.ShortLinkRemoteService;
import com.cjm.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.cjm.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.cjm.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.cjm.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.cjm.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cjm.shortlink.admin.common.convention.result.Result;
/**
 * 回收站管理-controller层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    private Result<Void> saveRecycleBin(@RequestParam RecycleBinSaveReqDTO requestParam) {
        shortLinkRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageRecycleBinShortLink(requestParam);
    }
}
