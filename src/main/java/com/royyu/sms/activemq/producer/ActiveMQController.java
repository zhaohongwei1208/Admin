package com.royyu.sms.activemq.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/mq")
@RestController
public class ActiveMQController {

    //可以读取在application.properties配置文件中的配置项 也可以和以前一样使用@Value("${}")
    @Autowired
    private Environment environment;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @GetMapping("/hello")
    public String SayHello(){
        return "Hello Spring Boot." + environment.getProperty("url");
    }

    @GetMapping("/send")
    public String sayHello(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", 123L);
        map.put("name", "传智播客");

        jmsMessagingTemplate.convertAndSend("spring.boot.queue", map);
        return "发送MQ队列消息成功！";
    }

    @GetMapping("/sendSms")
    public String sendSmsMsg(){
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", "17611243220");
        map.put("signName", "hongwei");
        map.put("templateCode", "SMS_135790743");
        map.put("templateParam", "{\"name\":\"宝星\"}");

        jmsMessagingTemplate.convertAndSend("itcast_sms_queue", map);
        return "发送短信消息成功！";
    }
}
