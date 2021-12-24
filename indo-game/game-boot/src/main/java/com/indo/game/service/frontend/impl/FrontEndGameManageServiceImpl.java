package com.indo.game.service.frontend.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.result.Result;
import com.indo.game.mapper.manage.GameCategoryMapper;
import com.indo.game.mapper.manage.GamePlatformMapper;
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

    public Result queryAllGameCategory(){
        LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(GameCategory::getSortNumber);
        List<GameCategory> categoryList = gameCategoryMapper.selectList(wrapper);
        return Result.success(categoryList);
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
}
