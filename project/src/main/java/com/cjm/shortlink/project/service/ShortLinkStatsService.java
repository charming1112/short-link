package com.cjm.shortlink.project.service;

import com.cjm.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.cjm.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

public interface ShortLinkStatsService {
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);
}
