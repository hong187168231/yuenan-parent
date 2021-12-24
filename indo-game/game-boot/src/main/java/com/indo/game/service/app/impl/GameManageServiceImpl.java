package com.indo.game.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.result.Result;
import com.indo.game.mapper.GameCategoryMapper;
import com.indo.game.mapper.GameCurrencyTypeMapper;
import com.indo.game.mapper.GameLanguageTypeMapper;
import com.indo.game.mapper.GamePlatformMapper;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameCurrencyType;
import com.indo.game.pojo.entity.manage.GameLanguageType;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.app.IGameManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public Result<GameCategory> queryAllGameCategory() {
        Result<GameCategory> result = new Result<>();
        LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
        List<GameCategory> categoryList = gameCategoryMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
    }


    public Result<List<GamePlatform>> queryAllGamePlatform() {
        Result<List<GamePlatform>> result = new Result<List<GamePlatform>>();
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
    }

    public Result<List<GamePlatform>> queryHotGamePlatform() {
        Result<List<GamePlatform>> result = new Result<List<GamePlatform>>();
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getIsHotShow, "1");
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
    }

    public boolean addGamePlatform(GamePlatform platform) {
        return gamePlatformMapper.insert(platform) > 0;
    }

    public boolean deleteBatchGamePlatform(Set<Long> ids) {
        return gamePlatformMapper.deleteBatchIds(ids) > 0;
    }

    public boolean modifiyGamePlatform(GamePlatform platform) {
        return gamePlatformMapper.updateById(platform) > 0;
    }

    public Result<GameLanguageType> queryLanguageType() {
        Result<GameLanguageType> result = new Result<GameLanguageType>();
        LambdaQueryWrapper<GameLanguageType> wrapper = new LambdaQueryWrapper<>();
        List<GameLanguageType> categoryList = gameLanguageTypeMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
    }

    public Result<GameCurrencyType> queryGameCurrencyType() {
        Result<GameCurrencyType> result = new Result<GameCurrencyType>();
        LambdaQueryWrapper<GameCurrencyType> wrapper = new LambdaQueryWrapper<>();
        List<GameCurrencyType> categoryList = gameCurrencyTypeMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
    }


    public Result queryGamePlatformByCategory(Long categoryId){
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getCategoryId,categoryId);
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        return Result.success(categoryList);
    }
}
