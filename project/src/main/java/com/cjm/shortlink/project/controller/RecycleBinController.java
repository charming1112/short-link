package com.cjm.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjm.shortlink.project.common.convention.result.Result;
import com.cjm.shortlink.project.common.convention.result.Results;
import com.cjm.shortlink.project.dto.req.*;
import com.cjm.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.cjm.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 回收站管理-controller层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 保存回收站
     * @param requestParam 参数
     * @return 返回结果
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    private Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        return Results.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam){
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 移除短链接
     */
    @PostMapping("/api/short-link/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam){
        recycleBinService.removeRecycleBin(requestParam);
        return Results.success();
    }
}
