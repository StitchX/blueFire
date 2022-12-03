package com.fire.common.tencent;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.common.config.TencentConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云COS工具类
 *
 * @author DK 2022年02月18日 11:34
 */
@Component
@Data
@Slf4j
public class TencentCos {


    @Resource
    private TencentConfig tencentConfig;


    public void download(String localFilePath, String tencentFilePath) {

        log.info("从腾讯云下载文件开始，本地文件地址：[{}],腾讯云文件地址:[{}]", localFilePath, tencentFilePath);

        TransferManager transferManager = createTransferManager();

        File downloadFile = new File(localFilePath);

        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(tencentConfig.getBucketName(), tencentFilePath);
            // 返回一个异步结果 Donload, 可同步的调用 waitForCompletion 等待下载结束, 成功返回 void, 失败抛出异常
            Download download = transferManager.download(getObjectRequest, downloadFile);
            download.waitForCompletion();
        } catch (Exception e) {
            log.error("下载文件异常，腾讯云文件地址：" + tencentFilePath, e);
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 关闭 TransferManager
        shutdownTransferManager(transferManager);

        log.info("从腾讯云下载文件完毕，本地文件地址：[{}],腾讯云文件地址:[{}]", localFilePath, tencentFilePath);

        //return download;
    }


    /**
     * 文件流形式上传文件到腾讯云COS
     *
     * @param inputStream     输入文件流
     * @param tencentFilePath 腾讯云COS存储地址
     * @return 腾讯云COS线上 url 地址
     */
    public String uploadStream(InputStream inputStream, String tencentFilePath) {
        log.info("上传文件到腾讯云cos开始,腾讯云文件地址:[{}]", tencentFilePath);
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager();


        ObjectMetadata objectMetadata = new ObjectMetadata();

        String url = "";
        try {
            // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
            // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
            objectMetadata.setContentLength(inputStream.available());
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            PutObjectRequest putObjectRequest = new PutObjectRequest(tencentConfig.getBucketName(), tencentFilePath, inputStream, objectMetadata);
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            ObjectMapper mapper = new ObjectMapper();
            log.info("COS上传结果：{}", mapper.writeValueAsString(uploadResult));
            url = "https://image.supervision.bluefire.top/".concat(tencentFilePath);
        } catch (Exception e) {
            log.error("文件流形式上传文件到腾讯云异常", e);
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 关闭 TransferManager
        shutdownTransferManager(transferManager);

        log.info("上传文件到腾讯云cos结束,腾讯云文件地址:[{}],腾讯云COS地址：[{}]", tencentFilePath, url);

        return url;
    }


    /**
     * 上传文件到腾讯云COS
     *
     * @param file            上传的文件
     * @param tencentFilePath 上传文件到腾讯云COS地址
     * @return 腾讯云COS线上 url 地址
     */
    public String uploadFile(File file, String tencentFilePath) {

        log.info("上传文件到腾讯云cos开始，本地文件名：[{}],腾讯云文件地址:[{}]", file.getName(), tencentFilePath);

        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager();


        String url = "";
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(tencentConfig.getBucketName(), tencentFilePath, file);
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            ObjectMapper ob = new ObjectMapper();
            log.info("上传文件到腾讯云，本地文件名：[{}],腾讯云文件地址:[{}],上传文件结果[{}]", file.getName(), tencentFilePath, ob.writeValueAsString(uploadResult));
            url = "https://image.supervision.bluefire.top/".concat(tencentFilePath);

            //System.out.println(mapper.writeValueAsString(uploadResult));
        } catch (Exception e) {
            log.error("上传文件异常,文件名：" + file.getName(), e);
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 关闭 TransferManager
        shutdownTransferManager(transferManager);

        log.info("上传文件到腾讯云cos结束，本地文件名：[{}],腾讯云文件地址:[{}],腾讯云COS地址：[{}]", file.getName(), tencentFilePath, url);

        return url;
    }


    /**
     * 创建 TransferManager 实例，这个实例用来后续调用高级接口
     *
     * @return TransferManager
     */
    private TransferManager createTransferManager() {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient
        TransferManager transferManager = null;
        try {
            COSClient cosClient = createCOSClient();

            // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
            // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
            ExecutorService threadPool = Executors.newFixedThreadPool(32);

            // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
            transferManager = new TransferManager(cosClient, threadPool);

            // 设置高级接口的配置项
            // 分块上传阈值和分块大小分别为 5MB 和 1MB
            TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
            transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
            transferManagerConfiguration.setMinimumUploadPartSize(5 * 1024 * 1024);
            transferManager.setConfiguration(transferManagerConfiguration);
        } catch (Exception e) {
            log.error("创建transferManager异常：", e);
        }

        return transferManager;
    }


    /**
     * 创建 COS 客户端
     *
     * @return cosClient
     */
    private COSClient createCOSClient() {
        COSClient cosClient = null;
        try {
            // 1 初始化用户身份信息（secretId, secretKey）。
            COSCredentials cred = new BasicCOSCredentials(tencentConfig.getSecretId(), tencentConfig.getSecretKey());

            // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。

            Region region = new Region(tencentConfig.getRegionName());
            ClientConfig clientConfig = new ClientConfig(region);


            // 这里建议设置使用 https 协议
            // 从 5.6.54 版本开始，默认使用了 https
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 设置最大重试次数为 5 次 默认为3次
            clientConfig.setMaxErrorRetry(5);

            // 3 生成 cos 客户端。
            cosClient = new COSClient(cred, clientConfig);
        } catch (Exception e) {
            log.error("创建cosClient异常：", e);
        }


        return cosClient;
    }

    /**
     * 关闭cos客户端
     *
     * @param cosClient
     */
    private void closeCOSClient(COSClient cosClient) {

        cosClient.shutdown();
    }

    /**
     * 关闭TransferManager
     *
     * @param transferManager
     */
    private void shutdownTransferManager(TransferManager transferManager) {

        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。

        try {
            transferManager.shutdownNow(true);
        } catch (Exception e) {
            log.error("关闭transferManager异常：", e);
        }
    }

}
