package com.cjm.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjm.shortlink.admin.dao.entity.GroupDO;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.cjm.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {
    void saveGroup(String name);

    void saveGroup(String username, String groupName);

    List<ShortLinkGroupRespDTO> listGroup();

    void update(ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO);

    void deleteGroup(String gid);

    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);
}
