package com.blue.business.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.blue.business.config.NoteConfig;
import com.blue.business.dto.NoteDto;
import com.blue.business.service.SmsServer;
import com.blue.business.util.RedisUtil;
import com.fire.common.exception.BaseException;
import com.fire.dto.enums.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description:
 * @ClassName: SmsServerImpl
 * @Author: liuliu
 * @Date: 2022/3/21 11:35
 */
@Service
@Slf4j
public class SmsServerImpl implements SmsServer {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisUtil redisUtil;

    private static final HttpHeaders headers;

    static {
        headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    }

    @Override
    public void sendMsg(String phone) {
        if (StrUtil.isEmpty(phone)) {
            throw new BaseException("手机号码不能为空");
        }
        if (!cn.hutool.core.util.PhoneUtil.isMobile(phone)) {
            throw new BaseException("手机号码错误");
        }
        String code = RandomUtil.randomNumbers(6);
        NoteDto noteDto = createNoteDto(phone, code);
        log.info("发送账号登录短信验证码，发送号码：{}，发送参数：{}", phone, JSONUtil.parseObj(noteDto));
        HttpEntity request = new HttpEntity(noteDto, headers);
        JSONObject result = restTemplate.postForObject(NoteConfig.url, request, JSONObject.class);
        log.info("登录账号用户手机号为：{}，响应参数为：{}", phone, result);
        if (!NoteConfig.result.equals(result.get("result").toString())) {
            throw new BaseException("获取短信验证码失败");
        }
        redisUtil.hset(RedisKey.BUSINESS_LGOIN_INCR.key(), phone, code, 300L);
    }

    private NoteDto createNoteDto(String phone, String code) {
        NoteDto dto = NoteDto.builder().account(NoteConfig.account)
                .pswd(NoteConfig.pswd)
                .ext(NoteConfig.ext)
                .rf(NoteConfig.rf)
                .pn(phone).build();
        dto.setMsg(dto.formatMsg(NoteConfig.msgPrefix, NoteConfig.msgSuffix, code));
        return dto;
    }


}
