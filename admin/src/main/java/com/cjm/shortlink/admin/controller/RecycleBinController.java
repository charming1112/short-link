package com.cjm.shortlink.admin.controller;

import com.cjm.shortlink.admin.common.convention.result.Results;
import com.cjm.shortlink.admin.remote.ShortLinkRemoteService;
import com.cjm.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import lombok.RequiredArgsConstructor;
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
}
