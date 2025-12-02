package com.cjm.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjm.shortlink.project.dao.entity.ShortLinkDO;
import com.cjm.shortlink.project.dto.req.RecycleBinSaveReqDTO;

public interface RecycleBinService extends IService<ShortLinkDO> {
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);
}
