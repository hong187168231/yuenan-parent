package com.live.admin.modules.file.controller;

import com.live.admin.modules.file.service.IFileService;
import com.live.admin.pojo.entity.FileInfo;
import com.live.common.mybatis.base.PageResult;
import com.live.common.result.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 文件上传
 *
 * @author 作者 owen E-mail: 624191343@qq.com
 */
@RestController
public class FileController {
    @Resource
    private IFileService fileService;

    /**
     * 文件上传
     * 根据fileType选择上传方式
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/files-anon")
    public FileInfo upload(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.upload(file);
    }

    /**
     * 文件删除
     *
     * @param id
     */
    @DeleteMapping("/files/{id}")
    public Result delete(@PathVariable String id) {
        try {
            fileService.delete(id);
            return Result.success("操作成功");
        } catch (Exception ex) {
            return Result.failed("操作失败");
        }
    }

    /**
     * 文件查询
     *
     * @param params
     * @return
     */
    @GetMapping("/files")
    public PageResult<FileInfo> findFiles(@RequestParam Map<String, Object> params) {
        return fileService.findList(params);
    }
}
