package com.indo.game.service.app.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.pojo.criteria.GameDownloadQueryCriteria;
import com.indo.common.constant.RedisConstants;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.QueryHelpPlus;
import com.indo.game.mapper.frontend.GameDownloadMapper;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.service.app.IGameDownloadService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameDownloadServiceImpl extends ServiceImpl<GameDownloadMapper, GameDownload> implements IGameDownloadService {


    public List<GameDownload> queryAllGameDownload() {
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_DOWNLOAD_KEY);
        List<GameDownload> gameDownloads = new ArrayList(map.values());
        if (ObjectUtil.isEmpty(gameDownloads)) {
            GameDownloadQueryCriteria criteria = new GameDownloadQueryCriteria();
            gameDownloads = baseMapper.selectList(QueryHelpPlus.getPredicate(GameDownload.class, criteria));
        }
        gameDownloads.sort(Comparator.comparing(GameDownload::getUpdateTime));
        return gameDownloads;
    }

}
