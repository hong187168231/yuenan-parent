package com.indo.msg.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.BrokerMessageStatus;
import com.indo.common.pojo.entity.BaseEntity;
import com.indo.msg.mapper.BrokerMessageMapper;
import com.indo.msg.pojo.entity.BrokerMessage;
import com.indo.msg.service.IBrokerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 消息记录表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-01-03
 */
@Service
public class BrokerMessageServiceImpl extends ServiceImpl<BrokerMessageMapper, BrokerMessage> implements IBrokerMessageService {

    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    @Override
    public boolean saveMessage(BrokerMessage message) {
        int row = this.baseMapper.insert(message);
        if (row > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void succuess(String messageId) {
        BrokerMessage brokeryMessage = new BrokerMessage();
        brokeryMessage.setMessageId(messageId);
        brokeryMessage.setStatus(BrokerMessageStatus.SEND_OK.getCode());
        this.baseMapper.updateById(brokeryMessage);
    }

    @Override
    public void failure(String messageId) {
        BrokerMessage brokeryMessage = new BrokerMessage();
        brokeryMessage.setMessageId(messageId);
        brokeryMessage.setStatus(BrokerMessageStatus.SEND_FAIL.getCode());
        this.baseMapper.updateById(brokeryMessage);
    }

    public List<BrokerMessage> queryTimeoutMessage4Retry(BrokerMessageStatus brokerMessageStatus) {
        List<BrokerMessage> list = brokerMessageMapper.queryBrokeryMessageStatus4Timeout(brokerMessageStatus.getCode());
        return list;
    }

    @Override
    public BrokerMessage selectByMessageId(String messageId) {
        return this.baseMapper.selectById(messageId);
    }

    @Override
    public void updateTryCount(String messageId) {
        brokerMessageMapper.updateForTryCount(messageId, new Date());
    }
}
