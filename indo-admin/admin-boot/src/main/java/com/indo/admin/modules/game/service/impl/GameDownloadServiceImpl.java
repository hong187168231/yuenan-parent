package com.indo.admin.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.game.mapper.*;
import com.indo.admin.modules.game.service.IGameDownloadService;
import com.indo.admin.modules.game.service.IGameManageService;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GameDownloadServiceImpl extends ServiceImpl<GameDownloadMapper, GameDownload> implements IGameDownloadService {


    public List<GameDownload> queryAllGameDownload() {
        LambdaQueryWrapper<GameDownload> wrapper = new LambdaQueryWrapper<>();
        List<GameDownload> categoryList = this.baseMapper.selectList(wrapper);
        return categoryList;
    }

    public boolean addGameDownload(GameDownload gameDownload) {
        return this.baseMapper.insert(gameDownload) > 0;
    }

    public boolean deleteBatchGameDownload(Set<Long> ids) {
        return this.baseMapper.deleteBatchIds(ids) > 0;
    }

    public boolean modifiyGameDownload(GameDownload gameDownload) {
        return this.baseMapper.updateById(gameDownload) > 0;
    }


}
