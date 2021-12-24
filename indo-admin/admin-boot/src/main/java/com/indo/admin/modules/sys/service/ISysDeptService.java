package com.indo.admin.modules.sys.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.admin.pojo.entity.SysDept;
import com.indo.admin.pojo.vo.DeptVO;
import com.indo.admin.pojo.vo.TreeVO;
import com.indo.common.mybatis.base.service.SuperService;

import java.util.List;

public interface ISysDeptService extends SuperService<SysDept> {

    List<DeptVO> listDeptVO(LambdaQueryWrapper<SysDept> baseQuery);

    List<TreeVO> listTreeVO(LambdaQueryWrapper<SysDept> baseQuery);
}
