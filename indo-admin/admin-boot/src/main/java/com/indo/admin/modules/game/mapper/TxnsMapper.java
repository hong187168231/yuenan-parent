package com.indo.admin.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.pojo.dto.BeforeDayDTO;
import com.indo.admin.pojo.dto.game.manage.GameInfoPageReq;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.admin.pojo.vo.game.manage.GameStatiRecord;
import com.indo.core.pojo.entity.game.Txns;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TxnsMapper extends BaseMapper<Txns> {

    List<GameStatiRecord> queryAllGameInfoCount(@Param("page") IPage<GameStatiRecord> page, @Param("req") GameInfoPageReq req);

    List<GameInfoRecord> queryAllGameInfo(@Param("page") IPage<GameInfoRecord> page, @Param("req") GameInfoPageReq req);

    List<BeforeDayDTO> beforeDayBetList(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
