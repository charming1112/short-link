package com.cjm.shortlink.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjm.shortlink.admin.common.convention.result.Result;
import com.cjm.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.cjm.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

public interface RecycleBinService {

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数包装
     */
    Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
