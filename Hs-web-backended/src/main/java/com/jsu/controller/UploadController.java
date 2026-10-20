package com.jsu.controller;

import com.jsu.anno.LogOperation;
import com.jsu.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Value("${tlias.upload.dir:D:/images/}")
    private String uploadDir;

    /**
     * 上传文件 - 参数名file
     */
    @PostMapping("/upload")
    @LogOperation
    public Result upload(MultipartFile file) throws Exception {
        log.info("上传文件：{}", file.getOriginalFilename());
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            return Result.error("文件名不能为空");
        }
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extName;
        // 拼接完整的文件路径
        File targetFile = new File(uploadDir + File.separator + uniqueFileName);

        // 如果目标目录不存在，则创建它
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        // 保存文件
        file.transferTo(targetFile);

        // 返回可访问的图片URL路径
        String imageUrl = "/images/" + uniqueFileName;
        return Result.success(imageUrl);
    }
}