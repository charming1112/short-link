package com.cjm.shortlink.admin.controller;

import com.cjm.shortlink.admin.common.convention.result.Result;
import com.cjm.shortlink.admin.common.convention.result.Results;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.cjm.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.cjm.shortlink.admin.service.GroupService;
import org.apache.shardingsphere.sql.parser.autogen.OpenGaussStatementParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    GroupService groupService;

    /**
     * 新增短链接分组
     * @param shortLinkGroupSaveReqDTO
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO shortLinkGroupSaveReqDTO){
        groupService.save(shortLinkGroupSaveReqDTO);
        return Results.success();
    }

    /**
     * 查询短链接分组
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup(){
        return Results.success(groupService.listGroup());
    }

    /**
     * 修改短链接分组
     * @param shortLinkGroupUpdateReqDTO
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/group")
    public Result<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO){

        groupService.update(shortLinkGroupUpdateReqDTO);
        return Results.success();
    }

    /**
     * 删除短链接分组
     * @param gid
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid){
        groupService.deleteGroup(gid);
        return Results.success();
    }

    /**
     * 短链接排序
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> requestParam) {
        groupService.sortGroup(requestParam);
        return Results.success();
    }
}
