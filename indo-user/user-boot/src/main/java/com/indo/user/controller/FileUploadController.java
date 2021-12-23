package com.indo.user.controller;

import com.indo.admin.api.FileFeignClient;
import com.indo.admin.pojo.entity.FileInfo;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文件上传
 * </p>
 *
 * @author puff
 * @since 2021-12-17
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/api/v1/file")
@Slf4j
@AllArgsConstructor
public class FileUploadController {


    @Resource
    private FileFeignClient fileFeignClient;

    /**
     * app文件上传
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "文件上传接口", httpMethod = "POST")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String result = fileFeignClient.upload(file);
        return Result.success(result);
    }

}
