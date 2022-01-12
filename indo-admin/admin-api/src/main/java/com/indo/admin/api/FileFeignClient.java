package com.indo.admin.api;

import com.indo.admin.api.fallback.FileFeignFallback;
import com.indo.admin.api.fallback.UserFeignFallback;
import com.indo.admin.pojo.entity.FileInfo;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.common.constant.ServiceIdConstant;
import com.indo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = ServiceIdConstant.ADMIN_SERVICE_ID, fallback = FileFeignFallback.class)
public interface FileFeignClient {

    @PostMapping(value = "/rpc/file/upload", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> upload(@RequestPart(value = "file") MultipartFile file);

}
