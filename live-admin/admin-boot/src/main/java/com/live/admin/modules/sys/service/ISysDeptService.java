package com.live.admin.modules.sys.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.live.admin.pojo.entity.SysDept;
import com.live.admin.pojo.vo.DeptVO;
import com.live.admin.pojo.vo.TreeVO;
import com.live.common.mybatis.base.service.SuperService;

import java.util.List;

public interface ISysDeptService extends SuperService<SysDept> {

    List<DeptVO> listDeptVO(LambdaQueryWrapper<SysDept> baseQuery);

    List<TreeVO> listTreeVO(LambdaQueryWrapper<SysDept> baseQuery);
}
