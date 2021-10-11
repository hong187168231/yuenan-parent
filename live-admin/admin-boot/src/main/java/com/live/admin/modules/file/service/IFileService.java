package com.live.admin.modules.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.live.admin.pojo.entity.FileInfo;
import com.live.common.mybatis.base.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.Map;

/**
 * 文件service
 *
 * @author 作者 owen E-mail: 624191343@qq.com
*/
public interface IFileService extends IService<FileInfo> {
	FileInfo upload(MultipartFile file ) throws Exception;
	
	PageResult<FileInfo> findList(Map<String, Object> params);

	void delete(String id);

	void out(String id, OutputStream os);
}
