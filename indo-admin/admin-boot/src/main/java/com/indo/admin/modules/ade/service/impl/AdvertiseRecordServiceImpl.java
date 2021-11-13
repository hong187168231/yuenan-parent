package com.indo.admin.modules.ade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.ade.mapper.AdvertiseRecordMapper;
import com.indo.admin.modules.ade.service.IAdvertiseRecordService;
import com.indo.admin.pojo.entity.AdvertiseRecord;
import com.indo.admin.pojo.vo.AdvertiseRecordVO;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
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
public class AdvertiseRecordServiceImpl extends ServiceImpl<AdvertiseRecordMapper, AdvertiseRecord> implements IAdvertiseRecordService {


    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Result<List<AdvertiseRecordVO>> queryList(AdvertiseRecordDTO pushRecordDTO) {
        Page<AdvertiseRecord> agentApplyPage = new Page<>(pushRecordDTO.getPage(), pushRecordDTO.getLimit());
        LambdaQueryWrapper<AdvertiseRecord> wrapper = new LambdaQueryWrapper<>();
        Page<AdvertiseRecord> pageList = this.baseMapper.selectPage(agentApplyPage, wrapper);
        List<AdvertiseRecordVO> result = dozerUtil.convert(pageList.getRecords(), AdvertiseRecordVO.class);
        return Result.success(result, agentApplyPage.getTotal());
    }

    @Override
    public boolean add(AdvertiseRecordDTO pushRecordAddDTO) {
        AdvertiseRecord pushRecord = new AdvertiseRecord();
        BeanUtils.copyProperties(pushRecordAddDTO, pushRecord);
        return baseMapper.insert(pushRecord) > 0;
    }

    @Override
    public boolean edit(AdvertiseRecordDTO pushRecordAddDTO) {
        AdvertiseRecord pushRecord = new AdvertiseRecord();
        BeanUtils.copyProperties(pushRecordAddDTO, pushRecord);
        return baseMapper.updateById(pushRecord) > 0;
    }
}
