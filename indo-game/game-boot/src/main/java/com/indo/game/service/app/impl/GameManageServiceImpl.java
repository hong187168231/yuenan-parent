package com.indo.game.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.utils.CollectionUtil;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GameParentPlatformMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.core.pojo.dto.game.manage.GameInfoPageImpReq;
import com.indo.core.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.core.pojo.vo.game.app.GameInfoAgentRecord;
import com.indo.core.pojo.vo.game.app.GameInfoRecord;
import com.indo.core.pojo.vo.game.app.GamePlatformRecord;
import com.indo.core.pojo.vo.game.app.GameStatiRecord;
import com.indo.game.service.app.IGameManageService;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameManageServiceImpl implements IGameManageService {

    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameParentPlatformMapper gameParentPlatformMapper;
    @Autowired
    private GameCategoryMapper gameCategoryMapper;
    @Autowired
    private TxnsMapper txnsMapper;
    @Autowired
    private GameCommonService gameCommonService;
    @Override
    public List<GameCategory> queryAllGameCategory() {
        List<GameCategory> categoryList;
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_CATEGORY_KEY);
        categoryList = new ArrayList(map.values());
        if (CollectionUtil.isEmpty(categoryList)) {
            LambdaQueryWrapper<GameCategory> wrapper = new LambdaQueryWrapper<>();
            categoryList = gameCategoryMapper.selectList(wrapper);
        }
        categoryList.sort(Comparator.comparing(GameCategory::getSortNumber));
        return categoryList;
    }

    @Override
    public List<GamePlatformRecord> queryAllGamePlatform() {
        List<GamePlatformRecord> platformList = gamePlatformMapper.queryAllGamePlatform();
        return platformList;
    }

    @Override
    public List<GamePlatformRecord> queryHotGamePlatform() {
        List<GamePlatformRecord> platformList = queryAllGamePlatform();
        if (CollectionUtil.isNotEmpty(platformList)) {
            platformList = platformList.stream()
                    .filter(platform -> platform.getIsHotShow().equals(1))
                    .collect(Collectors.toList());

        }
        return platformList;
    }
    @Override
    public List<GamePlatformRecord> queryGamePlatformByCategory(Long categoryId) {
        List<GamePlatformRecord> platformList = queryAllGamePlatform();
        if (CollectionUtil.isNotEmpty(platformList)) {
            platformList = platformList.stream()
                    .filter(platform -> platform.getCategoryId().equals(categoryId))
                    .collect(Collectors.toList());
        }
        return platformList;
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
    public Result<List<GameInfoAgentRecord>> queryAllAgentGameInfo(LoginInfo loginUser,GameInfoPageReq req) {
        boolean b = false;
        //验证是否是当前用户下代理
        GameInfoPageImpReq gameInfoPageImpReq = new GameInfoPageImpReq();
        BeanUtils.copyProperties(req, gameInfoPageImpReq);
        List agentAcctList = new ArrayList();
        if(null!=req && null!=req.getAgentAcct() && !"".equals(req.getAgentAcct())){
            MemTradingBO memTradingBO = gameCommonService.getMemTradingInfo(req.getAgentAcct());
            List<AgentRelation> agentRelationList = txnsMapper.queryAgentRelationByUserId(String.valueOf(loginUser.getId()),String.valueOf(memTradingBO.getId()));
            if(null!=agentRelationList && agentRelationList.size()>0){
               List<AgentRelation> subAgentRelationList = txnsMapper.queryAgentRelation(String.valueOf(memTradingBO.getId()));
                    if(null!=subAgentRelationList && subAgentRelationList.size()>0){
                        for(AgentRelation agentRelation : subAgentRelationList){
                            agentAcctList.add(agentRelation.getAccount());
                        }
                    }else {
                        return Result.success();
                    }
           }else {
                return Result.failed("g091035", "无效代理ID" );
           }
        }else {
            agentAcctList.add(loginUser.getAccount());
        }
        gameInfoPageImpReq.setAgentAcctList(agentAcctList);
        IPage<GameInfoAgentRecord> page = new Page<>(gameInfoPageImpReq.getPage(), gameInfoPageImpReq.getLimit());
        page.setRecords(txnsMapper.queryAllAgentGameInfo(page, gameInfoPageImpReq));
        return Result.success(page.getRecords(), page.getTotal());
    }

    @Override
    public List<GameParentPlatform> queryAllGameParentPlatform() {
        List<GameParentPlatform> parentPlatformList;
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_PARENT_PLATFORM_KEY);
        List list = new ArrayList(map.values());

        if (CollectionUtil.isEmpty(list)) {
            LambdaQueryWrapper<GameParentPlatform> wrapper = new LambdaQueryWrapper<>();
            parentPlatformList = gameParentPlatformMapper.selectList(wrapper);
        }else {
            parentPlatformList = (List<GameParentPlatform>)list.get(0);
        }
        if (CollectionUtil.isNotEmpty(parentPlatformList)) {
            parentPlatformList = parentPlatformList.stream()
                    .filter(parentPlatform -> parentPlatform.getIsStart().equals(1))
                    .collect(Collectors.toList());
        }
//        if (CollectionUtil.isNotEmpty(parentPlatformList)) {
//            parentPlatformList = parentPlatformList.stream()
//                    .filter(parentPlatform -> parentPlatform.getIsVirtual().equals("0"))
//                    .collect(Collectors.toList());
//        }
        parentPlatformList.sort(Comparator.comparing(GameParentPlatform::getSortNumber));
        return parentPlatformList;
    }

    @Override
    public List<GameParentPlatform> queryHotGameParentPlatform() {
        List<GameParentPlatform> platformList = this.queryAllGameParentPlatform();
        if (CollectionUtil.isNotEmpty(platformList)) {
            platformList = platformList.stream()
                    .filter(platform -> platform.getIsHotShow().equals(1))
                    .collect(Collectors.toList());
        }
        return platformList;
    }
}
