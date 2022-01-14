package com.indo.admin.modules.file.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.FileUtil;
import com.indo.admin.modules.file.mapper.FileMapper;
import com.indo.admin.modules.file.service.IFileService;
import com.indo.common.pojo.bo.ObjectInfo;
import com.indo.common.result.PageResult;
import com.indo.core.pojo.entity.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * AbstractIFileService 抽取类
 * file-server.type 实例化具体对象
 */
@Slf4j
public abstract class AbstractIFileService extends ServiceImpl<FileMapper, FileInfo> implements IFileService {
    private static final String FILE_SPLIT = ".";

    @Override
    public FileInfo upload(MultipartFile file, String folder) {
        FileInfo fileInfo = FileUtil.getFileInfo(file);
        if (!fileInfo.getName().contains(FILE_SPLIT)) {
            throw new IllegalArgumentException("缺少后缀名");
        }
        ObjectInfo objectInfo = uploadFile(file, folder);
        fileInfo.setPath(objectInfo.getObjectPath());
        fileInfo.setUrl(objectInfo.getObjectUrl());
        // 将文件信息保存到数据库
        baseMapper.insert(fileInfo);

        return fileInfo;
    }

    /**
     * 上传文件
     *
     * @param file
     */
    protected abstract ObjectInfo uploadFile(MultipartFile file, String folder);

    /**
     * 删除文件
     *
     * @param id 文件id
     */
    @Override
    public void delete(String id) {
        FileInfo fileInfo = baseMapper.selectById(id);
        if (fileInfo != null) {
            baseMapper.deleteById(fileInfo.getId());
            this.deleteFile(fileInfo.getPath());
        }
    }

    /**
     * 删除文件资源
     *
     * @param objectPath 文件路径
     */
    protected abstract void deleteFile(String objectPath);

    @Override
    public PageResult<FileInfo> findList(Map<String, Object> params) {
        Page<FileInfo> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<FileInfo> list = baseMapper.findList(page, params);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    public static void main(String[] args) {
        String fileName = "test.jpg";
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(fileSuffix);
    }
}
