package com.indo.admin.modules.ade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.ade.mapper.AdvertiseRecordMapper;
import com.indo.admin.modules.ade.service.IAdvertiseService;
import com.indo.admin.pojo.entity.Advertise;
import com.indo.admin.pojo.vo.AdvertiseVO;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.pojo.dto.AdvertiseQueryDTO;
import com.indo.user.pojo.dto.AdvertiseRecordDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 站内信 服务实现类
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
    public boolean add(AdvertiseRecordDTO pushRecordAddDTO) {
        Advertise pushRecord = new Advertise();
        BeanUtils.copyProperties(pushRecordAddDTO, pushRecord);
        return baseMapper.insert(pushRecord) > 0;
    }

    @Override
    public boolean edit(AdvertiseRecordDTO pushRecordAddDTO) {
        Advertise pushRecord = new Advertise();
        BeanUtils.copyProperties(pushRecordAddDTO, pushRecord);
        return baseMapper.updateById(pushRecord) > 0;
    }
}
