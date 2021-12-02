package com.indo.game.service.manage.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.result.Result;
import com.indo.game.mapper.manage.GameCategoryMapper;
import com.indo.game.mapper.manage.GameDownloadMapper;
import com.indo.game.mapper.manage.GamePlatformMapper;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.manage.IGameManageService;
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
    private GameDownloadMapper gameDownloadMapper;

    public Result<GameCategory> queryAllGameCategory(){
        Result<GameCategory> result = new Result<>();
        LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
        List<GameCategory> categoryList = gameCategoryMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
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

    public Result<List<GamePlatform>> queryAllGamePlatform(){
        Result<List<GamePlatform>> result = new Result<List<GamePlatform>>();
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
    }

    public Result<List<GamePlatform>> queryHotGamePlatform(){
        Result<List<GamePlatform>> result = new Result<List<GamePlatform>>();
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getIsHotShow,"1");
        List<GamePlatform> categoryList = gamePlatformMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
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

    public Result<GameDownload> queryAllGameDownload(){
        Result<GameDownload> result = new Result<GameDownload>();
        LambdaQueryWrapper<GameDownload> wrapper = new LambdaQueryWrapper<>();
        List<GameDownload> categoryList = gameDownloadMapper.selectList(wrapper);
        result.success(categoryList);
        return result;
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
}