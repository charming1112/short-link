package com.cjm.shortlink.project.controller;

import com.cjm.shortlink.project.common.convention.result.Result;
import com.cjm.shortlink.project.common.convention.result.Results;
import com.cjm.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.cjm.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
