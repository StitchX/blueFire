package com.blue.crm.service;

import cn.hutool.json.JSONObject;
import com.blue.crm.aliyun.ocr.OCRUtil;
import com.blue.crm.entity.FoodBusinessLicense;
import com.blue.crm.vo.BusinessLicenseVo;
import com.fire.common.exception.FileNameLengthLimitExceededException;
import com.fire.common.exception.InvalidExtensionException;
import com.fire.common.tencent.OSSUploadHelper;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Service
public class PictureUploadService {

    //营业执照COS路径前缀
    private static final String BUSINESS_LICENSE_PATH_PREFIX = "crm/businessLicense";
    //食品经营许可证COS路径前缀
    private static final String FOOD_BUSINESS_LICENSE_PATH_PREFIX = "crm/foodBusinessLicense";
    @Resource
    private OSSUploadHelper ossUploadHelper;
    @Resource
    private OCRUtil ocrUtil;

    public BaseRestResponse businessLicense(MultipartFile file) throws IOException {
        if (file == null) {
            return BaseRestResponse.error("文件不能为空！");
        }
        String imageUrl;
        try {
            imageUrl = ossUploadHelper.upload(file, BUSINESS_LICENSE_PATH_PREFIX, OSSUploadHelper.IMAGE_EXTENSION);
            log.info("上传营业执照到COS，地址为{}", imageUrl);
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            return BaseRestResponse.error("文件大小超出限制！");
        } catch (InvalidExtensionException e) {
            return BaseRestResponse.error("文件类型不支持！");
        } catch (FileNameLengthLimitExceededException e) {
            return BaseRestResponse.error("文件名大小超出限制！");
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return BaseRestResponse.error("图片上传失败！");
        }
        BusinessLicenseVo.BusinessLicenseVoBuilder builder = BusinessLicenseVo.builder();
        builder.imagesUrl(imageUrl);
        try {
            String responseData = ocrUtil.recognizeBusinessLicenseByUrl(imageUrl);
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject data = jsonObject.getJSONObject("营业执照识别实体信息");
            builder.storeName(data.getStr("企业名称"))
                    .businessScope(data.getStr("企业类型"))
                    .representName(data.getStr("企业法人"))
                    .socialCreditCode(data.getStr("企业统一社会信用编码"))
                    .detailedAddress(data.getStr("企业注册地址"));
        } catch (Exception e) {
            log.error("阿里云OCR识别营业执照信息失败 " + imageUrl, e);
            return BaseRestResponse.error("识别营业执照信息失败");
        }
        return new BaseRestResponse(builder.build());
    }

    public BaseRestResponse foodBusinessLicense(MultipartFile file) throws IOException {
        if (file == null) {
            return BaseRestResponse.error("文件不能为空！");
        }
        String imageUrl = null;
        try {
            imageUrl = ossUploadHelper.upload(file, FOOD_BUSINESS_LICENSE_PATH_PREFIX, OSSUploadHelper.IMAGE_EXTENSION);
            log.info("上传食品经营许可证到COS，地址为{}", imageUrl);
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            return BaseRestResponse.error("文件大小超出限制！");
        } catch (InvalidExtensionException e) {
            return BaseRestResponse.error("文件类型错误！");
        } catch (FileNameLengthLimitExceededException e) {
            return BaseRestResponse.error("文件名大小超出限制！");
        } catch (Exception e) {
            return BaseRestResponse.error("上传图片失败！");
        }
        FoodBusinessLicense.FoodBusinessLicenseBuilder builder = FoodBusinessLicense.builder();
        builder.imagesUrl(imageUrl);
        try {
            String responseData = ocrUtil.recognizeFoodBusinessLicenseByUrl(imageUrl);
            JSONObject data = new JSONObject(responseData).getJSONObject("食品经营许可证实体信息");
            builder
//                    .businessAddress(data.getStr("经营场所"))
                    .mainBusinessType(data.getStr("主体业态"))
                    .socialCreditCode(data.getStr("社会信用代码（身份证号码）"))
                    .issuingAuthority(data.getStr("发证机关"))
//                    .issueOfficer(data.getStr("签发人"))
                    .licenseKey(data.getStr("许可证编号"))
                    .enterpriseLegalPerson(data.getStr("法定代表人（负责人）"))
//                    .operatorName(data.getStr("经营者名称"))
                    .supervision(data.getStr("日常监督管理机构"))
//                    .regulatoryPersonnel(data.getStr("日常监督管理人员"))
//                    .officeAddress(data.getStr("住所"))
                    .validToDate(data.getStr("有效期限至"));
        } catch (Exception e) {
            log.error("阿里云OCR识别食品经营许可证信息失败,图片地址{},原因{} ", imageUrl, e.getMessage());
            return BaseRestResponse.error("识别食品经营许可证信息失败");
        }
        return new BaseRestResponse(builder.build());
    }
}
