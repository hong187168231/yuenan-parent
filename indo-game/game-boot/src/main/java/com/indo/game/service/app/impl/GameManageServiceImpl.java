package com.indo.game.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.result.Result;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.*;
import com.indo.game.pojo.vo.app.GameStatiRecord;
import com.indo.game.service.app.IGameManageService;
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
    private TxnsMapper txnsMapper;

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

    @Override
    public IPage<GameStatiRecord> queryAllGameInfo(GameInfoPageReq req) {
        IPage<GameStatiRecord> page = new Page<>(req.getPage(), req.getLimit());
        page = (IPage<GameStatiRecord>) txnsMapper.queryAllGameInfo(page,req);
        return page;
    }
}
