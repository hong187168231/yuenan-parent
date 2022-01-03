package com.indo.msg.mapper.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.msg.mapper.BrokerMessageMapper;
import com.indo.msg.pojo.entity.BrokerMessage;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class BrokerMessageMapperImpl implements BrokerMessageMapper {

    @Override
    public List<BrokerMessage> queryBrokeryMessageStatus4Timeout(Integer status) {
        return this.queryBrokeryMessageStatus4Timeout(status);
    }

    @Override
    public int updateForTryCount(String messageId, Date updateTime) {
        return this.updateForTryCount(messageId, updateTime);
    }

    @Override
    public int insert(BrokerMessage entity) {
        return 0;
    }

    @Override
    public int deleteById(Serializable id) {
        return 0;
    }

    @Override
    public int deleteByMap(Map<String, Object> columnMap) {
        return 0;
    }

    @Override
    public int delete(Wrapper<BrokerMessage> wrapper) {
        return 0;
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> idList) {
        return 0;
    }

    @Override
    public int updateById(BrokerMessage entity) {
        return 0;
    }

    @Override
    public int update(BrokerMessage entity, Wrapper<BrokerMessage> updateWrapper) {
        return 0;
    }

    @Override
    public BrokerMessage selectById(Serializable id) {
        return null;
    }

    @Override
    public List<BrokerMessage> selectBatchIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public List<BrokerMessage> selectByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public BrokerMessage selectOne(Wrapper<BrokerMessage> queryWrapper) {
        return null;
    }

    @Override
    public Integer selectCount(Wrapper<BrokerMessage> queryWrapper) {
        return null;
    }

    @Override
    public List<BrokerMessage> selectList(Wrapper<BrokerMessage> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<BrokerMessage> queryWrapper) {
        return null;
    }

    @Override
    public List<Object> selectObjs(Wrapper<BrokerMessage> queryWrapper) {
        return null;
    }

    @Override
    public <E extends IPage<BrokerMessage>> E selectPage(E page, Wrapper<BrokerMessage> queryWrapper) {
        return null;
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, Wrapper<BrokerMessage> queryWrapper) {
        return null;
    }
}
