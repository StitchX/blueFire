package com.fire.common.tencent;

import cn.hutool.core.util.RandomUtil;
import com.fire.common.exception.FileNameLengthLimitExceededException;
import com.fire.common.exception.InvalidExtensionException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * 文件上传操作助手
 */
@Component
public class OSSUploadHelper {
    // 默认的文件名最大长度
    public static final int DEFAULT_FILE_NAME_LENGTH = 200;
    //文件大小限制
    // 默认大小 50M
    public static final long DEFAULT_MAX_SIZE = 3145728;
    //允许扩展名
    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // pdf
            "pdf"};
    // 默认上传的地址
    public String[] allowedExtension = DEFAULT_ALLOWED_EXTENSION;
    //OCR支持识别的图片格式
    public static final String[] IMAGE_EXTENSION = {"bmp", "jpeg", "jpg", "jpe", "jp2", "png", "pbm", "pgm", "ppm", "tiff", "tif"};
    private long maxSize = DEFAULT_MAX_SIZE;

    @Resource
    private TencentCos ossClient;

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     *             添加出错信息
     * @return
     * @throws IOException
     * @throws FileNameLengthLimitExceededException
     * @throws InvalidExtensionException
     * @throws FileSizeLimitExceededException
     */
    public String upload(MultipartFile file, String baseDir)
            throws FileSizeLimitExceededException, InvalidExtensionException, FileNameLengthLimitExceededException,
            IOException {
        return upload(file, baseDir, allowedExtension);
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file             上传的文件
     *                         添加出错信息
     * @param allowedExtension 允许上传的文件类型
     * @return
     * @throws IOException
     * @throws FileNameLengthLimitExceededException
     * @throws InvalidExtensionException
     * @throws FileSizeLimitExceededException
     */
    public String upload(MultipartFile file, String baseDir, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException, FileNameLengthLimitExceededException,
            IOException {
        return upload(file, baseDir, allowedExtension, maxSize, true);
    }

    /**
     * 文件上传
     *
     * @param baseDir                   相对应用的基目录
     * @param file                      上传的文件
     * @param allowedExtension          允许的文件类型 null 表示允许所有
     * @param maxSize                   最大上传的大小 -1 表示不限制
     * @param needDatePathAndRandomName 是否需要日期目录和随机文件名前缀
     * @return 返回上传成功的文件名
     * @throws InvalidExtensionException            如果MIME类型不允许
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException                          比如读写文件出错时
     */
    public String upload(MultipartFile file, String baseDir,
                         String[] allowedExtension, long maxSize, boolean needDatePathAndRandomName)
            throws InvalidExtensionException, FileSizeLimitExceededException, IOException,
            FileNameLengthLimitExceededException {
        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > OSSUploadHelper.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(file.getOriginalFilename(), fileNamelength,
                    OSSUploadHelper.DEFAULT_FILE_NAME_LENGTH);
        }
        assertAllowed(file, allowedExtension, maxSize);
        String filename = extractFilename(file, baseDir, needDatePathAndRandomName);
        filename = StringUtils.trimDiagonal(filename);
        return ossClient.uploadStream(file.getInputStream(), filename);
    }

    public String extractFilename(MultipartFile file, String baseDir, boolean needDatePathAndRandomName) {
        String filename = file.getOriginalFilename();
        if (needDatePathAndRandomName) {
            filename = datePath() + System.currentTimeMillis() + RandomUtil.randomNumbers(3) + "."
                    + StringUtils.getExtensionName(filename);
        }
        if (!StringUtils.isEmpty(baseDir)) {
            filename = baseDir + "/" + filename;
        }
        return filename;
    }

    /**
     * 日期路径 即年/月/日 如2013/01/03
     *
     * @return
     */
    private String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 是否允许文件上传
     *
     * @param file             上传的文件
     * @param allowedExtension 文件类型 null 表示允许所有
     * @param maxSize          最大大小 字节为单位 -1表示不限制
     * @return
     * @throws InvalidExtensionException      如果MIME类型不允许
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public void assertAllowed(MultipartFile file, String[] allowedExtension, long maxSize)
            throws InvalidExtensionException, FileSizeLimitExceededException {

        String filename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            throw new InvalidExtensionException(allowedExtension, extension, filename);
        }

        long size = file.getSize();
        if (maxSize != -1 && size > maxSize) {
            throw new FileSizeLimitExceededException("文件大小超出限制", size, maxSize);
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.trim().equalsIgnoreCase(extension.trim())) {
                return true;
            }
        }
        return false;
    }
}