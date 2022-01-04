package com.indo.game.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.result.Result;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.entity.manage.*;
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
    public IPage<Txns> queryAllGameInfo(GameInfoPageReq req) {
        IPage<Txns> page = new Page<>(req.getPage(), req.getLimit());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getUserId,req.getUserAcct());
        if(null!=req.getPlatform()&&req.getPlatform().size()>0){//in
            wrapper.in(Txns::getPlatform,req.getPlatform());
        }
        if(null!=req.getCategoryId()&&req.getCategoryId().size()>0){//in
            wrapper.in(Txns::getCategoryId,req.getCategoryId());
        }
        if(null!=req.getStartTime()&&!"".equals(req.getStartTime())){//>=
            wrapper.ge(Txns::getCreateTime,req.getStartTime());
        }
        if(null!=req.getEndTime()&&!"".equals(req.getEndTime())){//<=
            wrapper.le(Txns::getCreateTime,req.getEndTime());
        }
        if(req.getOrderBy()){
            wrapper.orderByAsc(Txns::getCreateTime);
        }else {
            wrapper.orderByDesc(Txns::getCreateTime);
        }
        page = txnsMapper.selectPage(page,wrapper);
        return page;
    }
}
