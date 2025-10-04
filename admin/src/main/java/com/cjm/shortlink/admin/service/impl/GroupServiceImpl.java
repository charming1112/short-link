package com.cjm.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjm.shortlink.admin.common.biz.user.UserContext;
import com.cjm.shortlink.admin.dao.entity.GroupDO;
import com.cjm.shortlink.admin.dao.entity.UserDO;
import com.cjm.shortlink.admin.dao.mapper.GroupMapper;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.cjm.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.cjm.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.cjm.shortlink.admin.service.GroupService;
import com.cjm.shortlink.admin.toolkit.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {


    /**
     * 新增连接分组
     * @param shortLinkGroupSaveReqDTO
     */
    @Override
    public void save(ShortLinkGroupSaveReqDTO shortLinkGroupSaveReqDTO) {
        String gid;
        while (true){
            gid=RandomGenerator.generateRandom();
            if (!hasGroupDo(gid)){
                break;
            }
        }
        GroupDO groupDO = GroupDO.builder()
                .name(shortLinkGroupSaveReqDTO.getName())
                .username(UserContext.getUsername())
                .sortOrder(0)
                .gid(gid)
                .build();
        baseMapper.insert(groupDO);
    }

    /**
     * 查询短链接分组
     * @return
     */
    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {

        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);

        List<GroupDO> groupDOList = baseMapper.selectList(wrapper);

        return BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);


    }

    /**
     * 修改短链接分组
     * @param shortLinkGroupUpdateReqDTO
     */
    @Override
    public void update(ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO) {

        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, shortLinkGroupUpdateReqDTO.getGid())
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);

        GroupDO groupDO = new GroupDO();
        groupDO.setName(shortLinkGroupUpdateReqDTO.getName());
        baseMapper.update(groupDO,updateWrapper);
    }

    /**
     * 删除短链接分组
     * @param gid
     */

    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);
    }

    /**
     * 短链接分组排序
     * @param requestParam
     */
    @Override
    public void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam) {

        requestParam.forEach(each -> {
            GroupDO groupDO = GroupDO.builder()
                    .sortOrder(each.getSortOrder())
                    .build();
            LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getGid, each.getGid())
                    .eq(GroupDO::getDelFlag, 0);
            baseMapper.update(groupDO, updateWrapper);
        });
    }

    public Boolean hasGroupDo(String gid){
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class).eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO hasGroupDO = baseMapper.selectOne(wrapper);
        return hasGroupDO!=null;
    }

}
