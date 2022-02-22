package com.indo.admin.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.game.mapper.*;
import com.indo.admin.modules.game.service.IGameDownloadService;
import com.indo.admin.modules.game.service.IGameManageService;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.GameDownload;
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
        if (this.baseMapper.insert(gameDownload) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_DOWNLOAD_KEY, gameDownload.getId() + "", gameDownload);
            return true;
        }
        return false;
    }

    public boolean deleteBatchGameDownload(List<String> list) {
        if (this.baseMapper.deleteBatchIds(list) > 0) {
            list.forEach(id -> {
                AdminBusinessRedisUtils.hdel(RedisConstants.GAME_DOWNLOAD_KEY, id + "");
            });
            return true;
        }
        return false;
    }

    public boolean modifyGameDownload(GameDownload gameDownload) {
        if (this.baseMapper.updateById(gameDownload) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_DOWNLOAD_KEY, gameDownload.getId() + "", gameDownload);
            return true;
        }
        return false;
    }


}
