package com.indo.game.service.app.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.constant.RedisConstants;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.Activity;
import com.indo.core.pojo.entity.PayWithdrawConfig;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.*;
import com.indo.game.pojo.vo.app.GameInfoAgentRecord;
import com.indo.game.pojo.vo.app.GameInfoRecord;
import com.indo.game.pojo.vo.app.GameStatiRecord;
import com.indo.game.service.app.IGameManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameManageServiceImpl implements IGameManageService {

    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameCategoryMapper gameCategoryMapper;
    @Autowired
    private TxnsMapper txnsMapper;

    public List<GameCategory> queryAllGameCategory() {
        List<GameCategory> categoryList;
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_CATEGORY_KEY);
        categoryList = new ArrayList(map.values());
        if (ObjectUtil.isEmpty(categoryList)) {
            LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByAsc(GameCategory::getSortNumber);
            categoryList = gameCategoryMapper.selectList(wrapper);
        }
        categoryList.sort(Comparator.comparing(GameCategory::getSortNumber));
        return categoryList;
    }


    public List<GamePlatform> queryAllGamePlatform() {
        List<GamePlatform> platformList;
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_PLATFORM_KEY);
        platformList = new ArrayList(map.values());
        if (ObjectUtil.isEmpty(platformList)) {
            LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
            platformList = gamePlatformMapper.selectList(wrapper);
        }
        platformList.sort(Comparator.comparing(GamePlatform::getSortNumber));
        return platformList;
    }

    public List<GamePlatform> queryHotGamePlatform() {
        List<GamePlatform> platformList = queryAllGamePlatform();
        if (ObjectUtil.isNotEmpty(platformList)) {
            platformList = platformList.stream()
                    .filter(platform -> "1".equals(platform.getIsHotShow()))
                    .collect(Collectors.toList());
        }
        return platformList;
    }

    public List<GamePlatform> queryGamePlatformByCategory(Long categoryId) {
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getCategoryId, categoryId);
        wrapper.orderByAsc(GamePlatform::getSortNumber);
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        return categoryList;
    }

    @Override
    public IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req) {
        IPage<GameStatiRecord> page = new Page<>(req.getPage(), req.getLimit());
        page.setRecords(txnsMapper.queryAllGameInfoCount(page, req));
        return page;
    }

    @Override
    public IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req) {
        IPage<GameInfoRecord> page = new Page<>(req.getPage(), req.getLimit());
        page.setRecords(txnsMapper.queryAllGameInfo(page, req));
        return page;
    }

    @Override
    public IPage<GameInfoAgentRecord> queryAllAgentGameInfo(GameInfoPageReq req) {
        IPage<GameInfoAgentRecord> page = new Page<>(req.getPage(), req.getLimit());
        page.setRecords(txnsMapper.queryAllAgentGameInfo(page, req));
        return page;
    }
}
