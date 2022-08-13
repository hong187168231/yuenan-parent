package com.indo.core.mapper.game;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.core.pojo.dto.game.manage.GameInfoPageImpReq;
import com.indo.core.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.core.pojo.vo.game.app.GameInfoAgentRecord;
import com.indo.core.pojo.vo.game.app.GameInfoRecord;
import com.indo.core.pojo.vo.game.app.GameStatiRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TxnsMapper extends BaseMapper<Txns> {

    List<GameStatiRecord> queryAllGameInfoCount(@Param("page") IPage<GameStatiRecord> page, @Param("req") GameInfoPageReq req);

    List<GameInfoRecord> queryAllGameInfo(@Param("page") IPage<GameInfoRecord> page, @Param("req") GameInfoPageReq req);

    List<GameInfoAgentRecord> queryAllAgentGameInfo(@Param("page") IPage<GameInfoAgentRecord> page, @Param("req") GameInfoPageImpReq req);

    List<AgentRelation> queryAgentRelation( @Param("userId") String userId);

    List<AgentRelation> queryAgentRelationByUserId( @Param("userId") String userId,@Param("agentAcct") String agentAcct);

    String getMaxSortNo(@Param("platform") String platform);

}
