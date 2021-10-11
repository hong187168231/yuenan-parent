package com.indo.admin.modules.mem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemLevelService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.MemLevelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户等级表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Api(tags = "用户等级接口")
@RestController
@RequestMapping("/api/v1/memLevel")
public class MemLevelController {


    @Resource
    private IMemLevelService memLevelService;


    @ApiOperation(value = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long")
    })
    @GetMapping(value = "/listByPage")
    public Result<PageResult<MemLevelVo>> getMemLevels(Integer limit,
                                                       Integer page) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != limit && null != page) {
            pageNum = page;
            pageSize = limit;
        }
        Page<MemLevelVo> p =  new Page<>(pageNum, pageSize);
        List<MemLevelVo> list = memLevelService.selectByPage(p);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "全部查询")
    @GetMapping(value = "/list")
    public Result<List<MemLevel>> list(){
        List<MemLevel> list = memLevelService.list(new QueryWrapper<MemLevel>().lambda()
                .eq(MemLevel::getIsDel,0));
        return Result.success(list);
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Result create(@Validated @RequestBody MemLevel memLevel) {
        memLevelService.save(memLevel);
        return Result.success(HttpStatus.CREATED);
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/update")
    public Result update(@Validated @RequestBody MemLevel resources) {
        memLevelService.saveOrUpdate(resources);
        return Result.success(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        memLevelService.removeById(id);
        return Result.success(HttpStatus.OK);
    }

}
