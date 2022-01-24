package com.indo.admin.modules.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.game.mapper.*;
import com.indo.admin.modules.game.service.IGameManageService;
import com.indo.admin.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.admin.pojo.dto.game.manage.GamePlatformPageReq;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.admin.pojo.vo.game.manage.GameStatiRecord;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.manage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameManageServiceImpl implements IGameManageService {
    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameCategoryMapper gameCategoryMapper;
    @Autowired
    private GameLanguageTypeMapper gameLanguageTypeMapper;
    @Autowired
    private GameCurrencyTypeMapper gameCurrencyTypeMapper;
    @Autowired
    private TxnsMapper txnsMapper;

    public Result queryAllGameCategory() {
        LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
        List<GameCategory> categoryList = gameCategoryMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public boolean addGameCategory(GameCategory category) {
        return gameCategoryMapper.insert(category) > 0;
    }

    public boolean deleteBatchGameCategory(List<String> list) {
        return gameCategoryMapper.deleteBatchIds(list) > 0;
    }

    public boolean modifiyGameCategory(GameCategory category) {
        return gameCategoryMapper.updateById(category) > 0;
    }

    public IPage<GamePlatform> queryAllGamePlatform(GamePlatformPageReq req) {
        IPage<GamePlatform> page = new Page<>(null==req.getPage()?1: req.getPage(), null==req.getLimit()?10:req.getLimit());
        page.setRecords(gamePlatformMapper.queryAllGamePlatform(page,req));
        return page;

    }

    public Result<List<GamePlatform>> queryHotGamePlatform() {
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getIsHotShow, "1");
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public boolean addGamePlatform(GamePlatform platform) {
        return gamePlatformMapper.insert(platform) > 0;
    }

    public boolean deleteBatchGamePlatform(List<String> list) {
        return gamePlatformMapper.deleteBatchIds(list) > 0;
    }

    public boolean modifiyGamePlatform(GamePlatform platform) {
        return gamePlatformMapper.updateById(platform) > 0;
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
        IPage<GameStatiRecord> page = new Page<>(null==req.getPage()?1: req.getPage(), null==req.getLimit()?10:req.getLimit());
        page.setRecords(txnsMapper.queryAllGameInfoCount(page,req));
        return page;
    }

    @Override
    public IPage<GameInfoRecord> queryAllGameInfo(GameInfoPageReq req) {
        IPage<GameInfoRecord> page = new Page<>(null==req.getPage()?1: req.getPage(), null==req.getLimit()?10:req.getLimit());
        page.setRecords(txnsMapper.queryAllGameInfo(page,req));
        return page;
    }
}
