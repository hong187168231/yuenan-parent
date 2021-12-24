package com.indo.admin.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.indo.common.config.BucketNameConfig;
import com.indo.common.pojo.dto.BucketPolicyConfigDto;
import com.indo.common.pojo.dto.MinioUploadDto;
import com.indo.common.result.Result;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MinioUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioUtil.class);

    /**
     * 文件大小限制40M
     */
    private static final long FILEMAXSIZE = 50;

    @Value("${minio.endpoint}")
    private String url;
    @Value("${minio.imageBucketName}")
    private String imageBucketName;
    @Autowired
    private MinioClient minioClient;

    public Result uploadFiles(MultipartFile[] files, String fileBusinessType) {

        String urls = null;
        try {
            StringBuilder filefile = new StringBuilder();
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getOriginalFilename();
                fileName = BucketNameConfig.getNowTime() + fileName.substring(fileName.indexOf("."));
                //判断文件大小
                long size = files[i].getSize() / 1024 / 1024;
                if (size > FILEMAXSIZE) {
                    Result.failed("上传对图片大于：" + FILEMAXSIZE + "M限制");
                }
                //设置存储对象名称,封装文件路径
                StringBuilder sbFile = new StringBuilder();
                sbFile.append(fileBusinessType);
                sbFile.append(BucketNameConfig.FILE_SPLIT_PATH);
                sbFile.append(BucketNameConfig.getYear());
                sbFile.append(BucketNameConfig.FILE_SPLIT_PATH);
                sbFile.append(BucketNameConfig.getMonthAndDay());
                sbFile.append(BucketNameConfig.FILE_SPLIT_PATH);

                sbFile.append(fileName);
                fileName = sbFile.toString();

                // 使用putObject上传一个文件到存储桶中
                PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                        .bucket(imageBucketName)
                        .object(fileName)
                        .contentType(files[i].getContentType())
                        .stream(files[i].getInputStream(), files[i].getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build();
                minioClient.putObject(putObjectArgs);
                MinioUploadDto minioUploadDto = new MinioUploadDto();
                minioUploadDto.setName(fileName);
                minioUploadDto.setUrl(url + "/" + imageBucketName + "/" + fileName);
                filefile.append(minioUploadDto.getUrl());
                if(files.length>1){
                    filefile.append(",");
                }
                LOGGER.info("文件上传成功，地址：{}",minioUploadDto.getUrl());
            }
            urls = StrUtil.removeSuffix(filefile.toString(), ",");
        } catch (Exception e) {
            LOGGER.error("文件上传错误,业务场景："+fileBusinessType+"，异常："+e.getMessage());
            return Result.failed();
        }
        return Result.success(urls);
    }

    public Result deleteFile(String fileUrl) {

        try {
            String filePath = StrUtil.removePrefix(fileUrl, url + "/" + imageBucketName);
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(imageBucketName).object(filePath).build());
            LOGGER.info("文件删除成功，地址: {}", fileUrl);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("文件删除发生错误: {}", e.getMessage());
        }
        return Result.failed();
    }

    /**
     * 创建文件桶
     * @param bucketName
     * @return
     */
    private BucketPolicyConfigDto createBucketPolicyConfigDto(String bucketName) {
        BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement.builder()
                .Effect("Allow")
                .Principal("*")
                .Action("s3:GetObject")
                .Resource("arn:aws:s3:::"+bucketName+"/*.**").build();
        LOGGER.info("创建文件桶成功，桶名: {}！", bucketName);
        return BucketPolicyConfigDto.builder()
                .Version("2012-10-17")
                .Statement(CollUtil.toList(statement))
                .build();
    }
}
