package com.indo.game.service.frontend.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.result.Result;
import com.indo.game.mapper.manage.*;
import com.indo.game.pojo.entity.manage.*;
import com.indo.game.service.frontend.IFrontEndGameManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontEndGameManageServiceImpl implements IFrontEndGameManageService {
    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameCategoryMapper gameCategoryMapper;
    @Autowired
    private GameDownloadMapper gameDownloadMapper;
    @Autowired
    private GameLanguageTypeMapper gameLanguageTypeMapper;
    @Autowired
    private GameCurrencyTypeMapper gameCurrencyTypeMapper;

    public Result queryAllGameCategory(){
        LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(GameCategory::getSortNumber);
        List<GameCategory> categoryList = gameCategoryMapper.selectList(wrapper);
        return Result.success(categoryList);
    }
    public void addGameCategory(GameCategory category){
        gameCategoryMapper.insert(category);
    }

    public void deleteBatchGameCategory(List idList){
        gameCategoryMapper.deleteBatchIds(idList);
    }

    public void modifiyGameCategory(GameCategory category){
        gameCategoryMapper.updateById(category);
    }

    public Result queryAllGamePlatform(){
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(GamePlatform::getSortNumber);
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public Result queryHotGamePlatform(){
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getIsHotShow,"1");
        wrapper.orderByAsc(GamePlatform::getSortNumber);
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public Result queryGamePlatformByCategory(Long categoryId){
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getCategoryId,categoryId);
        wrapper.orderByAsc(GamePlatform::getSortNumber);
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public void addGamePlatform(GamePlatform platform){
        gamePlatformMapper.insert(platform);
    }

    public void deleteBatchGamePlatform(List idList){
        gamePlatformMapper.deleteBatchIds(idList);
    }

    public void modifiyGamePlatform(GamePlatform platform){
        gamePlatformMapper.updateById(platform);
    }

    public Result queryAllGameDownload(){
        LambdaQueryWrapper<GameDownload> wrapper = new LambdaQueryWrapper<>();
        List<GameDownload> categoryList = gameDownloadMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public void addGameDownload(GameDownload gameDownload){
        gameDownloadMapper.insert(gameDownload);
    }

    public void deleteBatchGameDownload(List idList){
        gameDownloadMapper.deleteBatchIds(idList);
    }

    public void modifiyGameDownload(GameDownload gameDownload){
        gameDownloadMapper.updateById(gameDownload);
    }

    public Result queryLanguageType(){
        LambdaQueryWrapper<GameLanguageType> wrapper = new LambdaQueryWrapper<>();
        List<GameLanguageType> categoryList = gameLanguageTypeMapper.selectList(wrapper);
        return Result.success(categoryList);
    }

    public Result queryGameCurrencyType(){
        LambdaQueryWrapper<GameCurrencyType> wrapper = new LambdaQueryWrapper<>();
        List<GameCurrencyType> categoryList = gameCurrencyTypeMapper.selectList(wrapper);
        return Result.success(categoryList);
    }
}
