package com.applet.controller;

import com.alibaba.fastjson.JSON;
import com.applet.annotation.SystemControllerLog;
import com.applet.entity.Sms;
import com.applet.utils.common.PostRequestUtils;
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
public class SMSController {

    private static final Integer TYPE = 1;

    @Autowired
    private Sms sms;


    @GetMapping("/cat")
    @SystemControllerLog(funcionExplain = "进入发送验证码控制层")
    public ResponseEntity<String> get(@RequestParam("phone") String phone) {

        try {
            Map<String, Object> m = new HashMap<>();
            m.put("phone", phone);
            m.put("markId", sms.getPrefix());
            m.put("smsType", TYPE);

            String s = PostRequestUtils.httpPostWithJSON(sms.getUrl(), JSON.toJSONString(m));
            return ResponseEntity.status(HttpStatus.OK).body(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/fish")
    @SystemControllerLog(funcionExplain = "进入验证验证码控制层")
    public ResponseEntity<String> check(@RequestParam("phone") String phone, @RequestParam("captchaNum") String captchaNum) {
        try {
            Map<String, Object> m = new HashMap<>();
            m.put("phone", phone);
            m.put("markId", sms.getPrefix());
            m.put("captchaNum", captchaNum);
            String s = PostRequestUtils.httpPostWithJSON(sms.getCheckUrl(), JSON.toJSONString(m));
            return ResponseEntity.status(HttpStatus.OK).body(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}