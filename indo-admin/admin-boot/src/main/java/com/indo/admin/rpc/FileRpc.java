package com.indo.admin.rpc;


import com.indo.admin.modules.file.service.IFileService;
import com.indo.admin.modules.msg.service.IMsgPushRecordService;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.FileInfo;
import com.indo.admin.pojo.entity.MsgPushRecord;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.common.result.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 系统参数 rpc控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/rpc/file")
public class FileRpc {


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
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        FileInfo fileInfo = fileService.upload(file,"app");
        return Result.success(fileInfo.getUrl());
    }


}    
    