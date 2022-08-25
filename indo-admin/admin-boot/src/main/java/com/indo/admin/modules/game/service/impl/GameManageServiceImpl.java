package com.indo.admin.modules.game.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.game.mapper.*;
import com.indo.admin.modules.game.service.IGameManageService;
import com.indo.admin.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.admin.pojo.dto.game.manage.GameParentPlatformPageReq;
import com.indo.admin.pojo.dto.game.manage.GamePlatformPageReq;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.admin.pojo.vo.game.manage.GameStatiRecord;
import com.indo.common.constant.RedisConstants;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.core.mapper.game.GameCategoryMapper;
import com.indo.core.pojo.entity.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GameManageServiceImpl implements IGameManageService {
    @Autowired
    private AdminGamePlatformMapper adminGamePlatformMapper;
    @Autowired
    private AdminGameParentPlatformMapper adminGameParentPlatformMapper;
    @Autowired
    private GameCategoryMapper gameCategoryMapper;
    @Autowired
    private GameLanguageTypeMapper gameLanguageTypeMapper;
    @Autowired
    private GameCurrencyTypeMapper gameCurrencyTypeMapper;
    @Autowired
    private AdminTxnsMapper adminTxnsMapper;

    public Result queryAllGameCategory() {
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_CATEGORY_KEY);
        LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
        List<GameCategory> categoryList = gameCategoryMapper.selectList(wrapper);
        if(ObjectUtil.isEmpty(map)){
            categoryList.forEach(l->{
                AdminBusinessRedisUtils.hset(RedisConstants.GAME_CATEGORY_KEY, l.getId() + "", l);
            });
        }
        return Result.success(categoryList);
    }

    public boolean addGameCategory(GameCategory category) {
        if (gameCategoryMapper.insert(category) > 0) {
            AdminBusinessRedisUtils.del(RedisConstants.GAME_CATEGORY_KEY);
            return true;
        }
        return false;
    }

    public boolean deleteBatchGameCategory(List<String> list) {
        if (gameCategoryMapper.deleteBatchIds(list) > 0) {
            AdminBusinessRedisUtils.del(RedisConstants.GAME_CATEGORY_KEY);
            return true;
        }
        return false;
    }

    public boolean modifyGameCategory(GameCategory category) {
        if (gameCategoryMapper.updateById(category) > 0) {
            AdminBusinessRedisUtils.del(RedisConstants.GAME_CATEGORY_KEY);
            return true;
        }
        return false;
    }

    public IPage<GamePlatform> queryAllGamePlatform(GamePlatformPageReq req) {
        IPage<GamePlatform> page = new Page<>(null == req.getPage() ? 1 : req.getPage(), null == req.getLimit() ? 10 : req.getLimit());
        page.setRecords(adminGamePlatformMapper.queryAllGamePlatform(page, req));
        return page;

    }

    public Result<List<GamePlatform>> queryHotGamePlatform() {
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getIsHotShow, 1);
        List<GamePlatform> categoryList = adminGamePlatformMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public boolean addGamePlatform(GamePlatform platform) {
        if (adminGamePlatformMapper.insert(platform) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_PLATFORM_KEY, platform.getId() + "", platform);
            return true;
        }
        return false;
    }

    public boolean deleteBatchGamePlatform(List<String> list) {
        if (adminGamePlatformMapper.deleteBatchIds(list) > 0) {
            list.forEach(id -> {
                AdminBusinessRedisUtils.hdel(RedisConstants.GAME_PLATFORM_KEY, id + "");
            });
            return true;
        }
        return false;
    }

    public boolean modifiyGamePlatform(GamePlatform platform) {
        if (adminGamePlatformMapper.updateById(platform) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_PLATFORM_KEY, platform.getId() + "", platform);
            return true;
        }
        return false;
    }

    public Result queryLanguageType() {
        LambdaQueryWrapper<GameLanguageType> wrapper = new LambdaQueryWrapper<>();
        List<GameLanguageType> categoryList = gameLanguageTypeMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public Result queryGameCurrencyType() {
        LambdaQueryWrapper<GameCurrencyType> wrapper = new LambdaQueryWrapper<>();
        List<GameCurrencyType> categoryList = gameCurrencyTypeMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    @Override
    public IPage<GameStatiRecord> queryAllGameInfoCount(GameInfoPageReq req) {
        IPage<GameStatiRecord> page = new Page<>(null == req.getPage() ? 1 : req.getPage(), null == req.getLimit() ? 10 : req.getLimit());
        page.setRecords(adminTxnsMapper.queryAllGameInfoCount(page, req));
        return page;
    }

    @Override
    public IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req) {
        IPage<GameInfoRecord> page = new Page<>(null == req.getPage() ? 1 : req.getPage(), null == req.getLimit() ? 10 : req.getLimit());
        page.setRecords(adminTxnsMapper.queryAllGameInfo(page, req));
        return page;
    }

    public IPage<GameParentPlatform> queryAllGameParentPlatform(GameParentPlatformPageReq req) {
        IPage<GameParentPlatform> page = new Page<>(null == req.getPage() ? 1 : req.getPage(), null == req.getLimit() ? 10 : req.getLimit());
        page.setRecords(adminGameParentPlatformMapper.queryAllGameParentPlatform(page, req));
        return page;

    }

    public Result<List<GameParentPlatform>> queryHotGameParentPlatform() {
        LambdaQueryWrapper<GameParentPlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameParentPlatform::getIsHotShow, 1);
        List<GameParentPlatform> categoryList = adminGameParentPlatformMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public boolean addGameParentPlatform(GameParentPlatform gameParentPlatform) {
        if (adminGameParentPlatformMapper.insert(gameParentPlatform) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_PARENT_PLATFORM_KEY, gameParentPlatform.getId() + "", gameParentPlatform);
            return true;
        }
        return false;
    }

    public boolean deleteBatchGameParentPlatform(List<String> list) {
        if (adminGameParentPlatformMapper.deleteBatchIds(list) > 0) {
            list.forEach(id -> {
                AdminBusinessRedisUtils.hdel(RedisConstants.GAME_PARENT_PLATFORM_KEY, id + "");
            });
            return true;
        }
        return false;
    }

    public boolean modifiyGameParentPlatform(GameParentPlatform gameParentPlatform) {
        if (adminGameParentPlatformMapper.updateById(gameParentPlatform) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_PARENT_PLATFORM_KEY, gameParentPlatform.getId() + "", gameParentPlatform);
            return true;
        }
        return false;
    }
}
