package com.cjm.shortlink.project.controller;

import com.cjm.shortlink.project.common.convention.result.Result;
import com.cjm.shortlink.project.common.convention.result.Results;
import com.cjm.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.cjm.shortlink.project.service.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接控制层
 */
@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;


    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> creatShortLink(@RequestBody ShortLinkCreateReqDTO shortLinkCreateReqDTO){
        return Results.success(shortLinkService.creatShortLink(shortLinkCreateReqDTO));
    }

}
