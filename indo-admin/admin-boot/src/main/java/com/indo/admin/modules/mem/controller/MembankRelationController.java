package com.indo.admin.modules.mem.controller;

import com.indo.admin.modules.mem.service.IMemBankRelationService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.MembankRelationDTO;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.vo.MemBankRelationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户银行卡管理Controller
 * </p>
 *
 * @author mzh
 * @since 2021-08-30
 */
@Api(tags = "用户银行卡管理接口")
@RestController
@RequestMapping("/api/v1/bank/relation")
public class MembankRelationController {
    @Autowired
    private IMemBankRelationService relationService;

    @ApiOperation(value = "分页查询用户银行卡信息")
    @GetMapping(value = "/list")
    public Result list(MembankRelationDTO membankRelationDTO){
        PageResult<MemBankRelationVO> result = relationService.queryList(membankRelationDTO);
        return Result.success(result);
    }

    @ApiOperation(value = "修改用户银行卡信息")
    @PostMapping(value = "/update")
    public Result update(@RequestBody MemBankRelation memBankRelation) {
        memBankRelation.setUpdateTime(new Date());
        boolean flag = relationService.saveOrUpdate(memBankRelation);
        if(flag){
            return Result.success(HttpStatus.OK);
        }else {
            return Result.failed();
        }

    }

    @ApiOperation(value = "导出用户银行卡信息")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response,@RequestParam(required = true) List<Long> ids) throws IOException {
        relationService.exportList(response,ids);
    }
}
