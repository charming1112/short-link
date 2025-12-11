package com.cjm.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjm.shortlink.project.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.cjm.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

public interface ShortLinkStatsService {
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);

    IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam);

    ShortLinkStatsRespDTO groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam);

    IPage<ShortLinkStatsAccessRecordRespDTO> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam);
}
