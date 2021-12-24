package com.indo.admin.modules.ade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.constant.SystemConstants;
import com.indo.admin.common.util.BusinessRedisUtils;
import com.indo.admin.modules.ade.mapper.AdvertiseRecordMapper;
import com.indo.admin.modules.ade.service.IAdvertiseService;
import com.indo.admin.pojo.entity.Advertise;
import com.indo.admin.pojo.vo.AdvertiseVO;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.pojo.dto.AdvertiseQueryDTO;
import com.indo.user.pojo.dto.AdvertiseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 广告 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Service
public class AdvertiseServiceImpl extends ServiceImpl<AdvertiseRecordMapper, Advertise> implements IAdvertiseService {


    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Result<List<AdvertiseVO>> queryList(AdvertiseQueryDTO queryDTO) {
        Page<Advertise> agentApplyPage = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        LambdaQueryWrapper<Advertise> wrapper = new LambdaQueryWrapper<>();
        Page<Advertise> pageList = this.baseMapper.selectPage(agentApplyPage, wrapper);
        List<AdvertiseVO> result = dozerUtil.convert(pageList.getRecords(), AdvertiseVO.class);
        return Result.success(result, agentApplyPage.getTotal());
    }

    @Override
    @Transactional
    public boolean add(AdvertiseDTO advertiseDTO) {
        Advertise advertise = new Advertise();
        BeanUtils.copyProperties(advertiseDTO, advertise);
        if (baseMapper.insert(advertise) > 0) {
            BusinessRedisUtils.hset(RedisConstants.ADMIN_ADVERTISING_KEY, advertise.getAdeId() + "", advertise);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean edit(AdvertiseDTO advertiseDTO) {
        Advertise advertise = new Advertise();
        BeanUtils.copyProperties(advertiseDTO, advertise);
        if (baseMapper.updateById(advertise) > 0) {
            BusinessRedisUtils.hset(RedisConstants.ADMIN_ADVERTISING_KEY, advertise.getAdeId() + "", advertise);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delAde(Long adeId) {
        if (this.baseMapper.deleteById(adeId) > 0) {
            BusinessRedisUtils.hdel(RedisConstants.ADMIN_ADVERTISING_KEY, adeId + "");
            return true;
        }
        return false;
    }

    @Override
    public boolean operateStatus(Long adeId, Integer status) {
        Advertise advertise = new Advertise();
        advertise.setStatus(status);
        advertise.setAdeId(adeId);
        if (this.baseMapper.updateById(advertise) > 0) {
            if (status.equals(SystemConstants.ADE_SHELVES)) {
                BusinessRedisUtils.hset(RedisConstants.ADMIN_ADVERTISING_KEY, advertise.getAdeId() + "", advertise);
            } else if (status.equals(SystemConstants.ADE_SOLD_OUT)) {
                BusinessRedisUtils.hdel(RedisConstants.ADMIN_ADVERTISING_KEY, adeId + "");
            }
            return true;
        }
        return false;
    }

}
