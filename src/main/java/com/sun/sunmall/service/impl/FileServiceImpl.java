package com.sun.sunmall.service.impl;

import com.google.common.collect.Lists;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.service.IFileService;
import com.sun.sunmall.utils.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by sun on 2017/5/18.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadImageFile(MultipartFile multipartFile, String path) {
        String fileName = multipartFile.getOriginalFilename();//拿到原始文件名 即不包含文件夹前缀D:/IMAGE/../ 只拿到abc.jpg
        String fileExtentionName = fileName.substring(fileName.lastIndexOf(".")+1); //不加1就拿到 .jpg  且从后开始找。 为了防止abc.abc.abc.jpg这种格式的文件
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtentionName;//防止重名覆盖
        logger.info("开始上传文件：{}",fileName);//{}用来在logger中占位

        File fileDir = new File(path);//新建上传服务器的目录
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();//mkdir ：只保留一个文件夹 /D 和mkdirs：保留所有文件夹 A/B/C/D的区别
        }
        File targetFile = new File(path, uploadFileName);
        try {
            multipartFile.transferTo(targetFile);//multipartFile 复制为 本地目标文件 待上传
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));//上传到FTP服务器
            targetFile.delete();//删除本地缓存文件
        } catch (IOException e) {
            logger.info("文件上传异常");
        }
        return targetFile.getName();
    }
}
