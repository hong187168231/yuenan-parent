package com.indo.admin.modules.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.PageResult;
import com.indo.core.pojo.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.util.Map;

/**
 * 文件service
 *
 * @author 作者 owen E-mail: 624191343@qq.com
 */
public interface IFileService extends IService<FileInfo> {
    FileInfo upload(MultipartFile file, String folder, HttpServletRequest request);

    PageResult<FileInfo> findList(Map<String, Object> params);

    void delete(String id);

    void out(String id, OutputStream os);
}
