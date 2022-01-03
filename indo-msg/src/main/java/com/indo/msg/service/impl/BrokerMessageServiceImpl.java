package com.indo.msg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.BrokerMessageStatus;
import com.indo.msg.mapper.BrokerMessageMapper;
import com.indo.msg.pojo.entity.BrokerMessage;
import com.indo.msg.service.IBrokerMessageService;
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


    private  BrokerMessageMapper brokerMessageMapper;

    @Override
    public boolean saveMessage(BrokerMessage message) {
        return this.baseMapper.insert(message) > 0;
    }

    @Override
    public void succuess(String messageId) {
        BrokerMessage brokeryMessage = new BrokerMessage();
        brokeryMessage.setMessageId(messageId);
        brokeryMessage.setStatus(BrokerMessageStatus.SEND_OK.getCode());
    }

    @Override
    public void failure(String messageId) {
        BrokerMessage brokeryMessage = new BrokerMessage();
        brokeryMessage.setMessageId(messageId);
        brokeryMessage.setStatus(BrokerMessageStatus.SEND_FAIL.getCode());
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
