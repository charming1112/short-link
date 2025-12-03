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
import org.springframework.web.bind.annotation.*;
import com.cjm.shortlink.admin.common.convention.result.Result;
import com.cjm.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
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

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }
}
