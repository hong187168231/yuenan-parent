package com.indo.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.game.pojo.dto.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.app.GameInfoAgentRecord;
import com.indo.game.pojo.vo.app.GameInfoRecord;
import com.indo.game.pojo.vo.app.GameStatiRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TxnsMapper extends BaseMapper<Txns> {

    List<GameStatiRecord> queryAllGameInfoCount(@Param("page") IPage<GameStatiRecord> page, @Param("req") GameInfoPageReq req);

    List<GameInfoRecord> queryAllGameInfo(@Param("page") IPage<GameInfoRecord> page, @Param("req") GameInfoPageReq req);

    List<GameInfoAgentRecord> queryAllAgentGameInfo(@Param("page") IPage<GameInfoAgentRecord> page, @Param("req") GameInfoPageReq req);

    String getMaxSortNo(@Param("platform") String platform);
}
