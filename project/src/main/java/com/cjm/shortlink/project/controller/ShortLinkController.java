package com.cjm.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjm.shortlink.project.common.convention.result.Result;
import com.cjm.shortlink.project.common.convention.result.Results;
import com.cjm.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.cjm.shortlink.project.service.ShortLinkService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接控制层
 */
@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;


    /**
     * 创建短链接
     * @param shortLinkCreateReqDTO
     * @return
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> creatShortLink(@RequestBody ShortLinkCreateReqDTO shortLinkCreateReqDTO){
        return Results.success(shortLinkService.creatShortLink(shortLinkCreateReqDTO));
    }

    /**
     * 分页查询短链接
     * @param shortLinkPageReqDTO
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO shortLinkPageReqDTO){

        return Results.success(shortLinkService.pageShortLink(shortLinkPageReqDTO));
    }

    /**
     * 查询传入gid的分组内短链接数量
     */
    @GetMapping("/api/short-link/v1/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam) {
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }

    /**
     * 修改短链接
     * @param shortLinkUpdateReqDTO
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO shortLinkUpdateReqDTO){
        shortLinkService.updateShortLink(shortLinkUpdateReqDTO);
        return Results.success();
    }

    /**
     * 短链接跳转
     * @param shortUri
     * @param request
     * @param response
     */

    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, ServletRequest request, ServletResponse response) {
        shortLinkService.restoreUrl(shortUri, request, response);
    }

}
