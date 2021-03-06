package com.applet.controller;

import com.alibaba.fastjson.JSON;
import com.applet.annotation.SystemControllerLog;
import com.applet.entity.Sms;
import com.applet.enums.ResultEnums;
import com.applet.utils.AppletResult;
import com.applet.utils.ResultUtil;
import com.applet.utils.common.PostRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SMSController{

    private static final Logger LOGGER= LoggerFactory.getLogger(SMSController.class);

    private static final Integer TYPE = 1;

    @Autowired
    private Sms sms;


    @GetMapping("/wx_xcx_cat")
    @SystemControllerLog(funcionExplain = "进入发送验证码控制层")
    public AppletResult get(@RequestParam("phone") String phone) {

        try {
            Map<String, Object> m = new HashMap<>();
            m.put("phone", phone);
            m.put("markId", sms.getPrefix());
            m.put("smsType", TYPE);

            String s = PostRequestUtils.httpPostWithJSON(sms.getUrl(), JSON.toJSONString(m));
            LOGGER.debug("发送短信的返回结果 {}",s);
            return ResultUtil.success();
        } catch (Exception e) {
            LOGGER.error("ERROR {}",e.getMessage());
            return ResultUtil.error(ResultEnums.SERVER_ERROR);
        }
    }

    @GetMapping("/wx_xcx_fish")
    @SystemControllerLog(funcionExplain = "进入验证验证码控制层")
    public AppletResult check(@RequestParam("phone") String phone, @RequestParam("captchaNum") String captchaNum) {
        try {
            Map<String, Object> m = new HashMap<>();
            m.put("phone", phone);
            m.put("markId", sms.getPrefix());
            m.put("captchaNum", captchaNum);
            String s = PostRequestUtils.httpPostWithJSON(sms.getCheckUrl(), JSON.toJSONString(m));
            LOGGER.debug("验证验证码的返回结果 {}",s);
            return ResultUtil.success();
        } catch (Exception e) {
            LOGGER.error("ERROR {}",e.getMessage());
            return ResultUtil.error(ResultEnums.SERVER_ERROR);
        }
    }
}
