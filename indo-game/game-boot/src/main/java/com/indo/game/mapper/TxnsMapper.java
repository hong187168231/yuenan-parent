package com.indo.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.game.pojo.entity.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.app.GameStatiRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TxnsMapper extends BaseMapper<Txns> {

    List<GameStatiRecord> queryAllGameInfo(@Param("page") IPage<GameStatiRecord> page, @Param("req") GameInfoPageReq req);
}
