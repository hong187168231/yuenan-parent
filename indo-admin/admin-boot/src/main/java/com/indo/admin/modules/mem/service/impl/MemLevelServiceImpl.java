package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.mapper.MemLevelMapper;
import com.indo.admin.pojo.req.mem.MemLevelAddReq;
import com.indo.admin.pojo.req.mem.MemLevelPageReq;
import com.indo.admin.pojo.req.mem.MemLevelUpdateReq;
import com.indo.admin.modules.mem.service.IMemLevelService;
import com.indo.core.pojo.vo.MemLevelVo;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.entity.MemLevel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户等级表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Service
public class MemLevelServiceImpl extends SuperServiceImpl<MemLevelMapper, MemLevel> implements IMemLevelService {

    @Autowired
    private MemLevelMapper memLevelMapper;

    @Override
    public Page<MemLevelVo> selectByPage(MemLevelPageReq req) {
        Page<MemLevelVo> page = new Page<>(req.getPage(), req.getLimit());
        List<MemLevelVo> list = memLevelMapper.listByMemLevel(page);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOne(MemLevelAddReq req) {
        MemLevel memLevel = new MemLevel();
        BeanUtils.copyProperties(req, memLevel);
        if (baseMapper.insert(memLevel) > 0) {
            refreshMemLevel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOne(MemLevelUpdateReq req, HttpServletRequest request) {
        MemLevel memLevel = baseMapper.selectById(req.getId());
        String countryCode = request.getHeader("countryCode");
        if (memLevel == null) {
            throw new BizException(MessageUtils.get(ResultCode.DATA_NONENTITY.getCode(),countryCode));
        }
        if(!memLevel.getLevel().equals(req.getLevel())){
            throw new BizException(MessageUtils.get(ResultCode.NOT_UPDATE_LEVEL.getCode(),countryCode));
        }
        MemLevel tempMemLevel = new MemLevel();
        BeanUtils.copyProperties(req, tempMemLevel);
        if (baseMapper.updateById(tempMemLevel) > 0) {
            refreshMemLevel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delMemLevel(Long id) {
        if (this.baseMapper.deleteById(id) > 0) {
            refreshMemLevel();
        }
        return false;
    }


    @Transactional
    void refreshMemLevel() {
        LambdaQueryWrapper<MemLevel> wrapper = new LambdaQueryWrapper();
        List<MemLevel> levelList = this.baseMapper.selectList(wrapper);
        wrapper.orderByDesc(MemLevel::getLevel);
        AdminBusinessRedisUtils.refreshMemLevel(levelList);
    }

}