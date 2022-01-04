package com.indo.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.game.pojo.entity.manage.GameInfoPageReq;
import com.indo.game.pojo.entity.manage.Txns;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TxnsMapper extends BaseMapper<Txns> {

    List<Txns> queryAllGameInfo(@Param("page") Page<Txns> page, @Param("req") GameInfoPageReq req);
}
