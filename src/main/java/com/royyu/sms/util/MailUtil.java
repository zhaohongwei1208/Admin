package com.royyu.sms.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MailUtil {
    @Value("${spring.mail.username}")
    private String username;
    private String to = "1078918763@qq.com";

    @Autowired
    private JavaMailSender sender;

    /**
     * 发送简单邮件
     */
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject("测试邮件");
        message.setText("hello world springboot mail");
        sender.send(message);
    }

    /**
     * 发送含附件的邮件内嵌图片，给定一个CID值即可，增加附件，
     * 使用MimeMessageHelper的addAttachment即可现在一般不会做内嵌图片，
     * 因为这样邮件会很大，容易对服务器造成压力，一般做法是使用图片链接另外
     */
    public void sendAttachmentMail() throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject("测试邮件");
        helper.setText("<body>hello springboot：<img src='cid:head'></img></body>", true); //内嵌静态图片资源

        FileSystemResource file1 = new FileSystemResource(new File("/Users/baidu/photo/1.jpeg"));
        //添加附件，这里第一个参数是在邮件中显示的名称，也可以直接是head.jpg，但是一定要有文件后缀，不然就无法显示图片了。
        helper.addAttachment("header.jpg", file1);
        helper.addInline("head", file1); //内嵌静态图片资源

//        FileSystemResource file2 = new FileSystemResource(new File("I:\\照片\\广州之旅\\IMG_0779.JPG"));
//        helper.addAttachment("header2.jpg", file2);

        log.info("开始发邮件===============");
        sender.send(message);
        log.info("发邮件结束===============");
    }

    /**
     * 发送模板邮件
     */
    public void sendTemplateEmail() throws Exception {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject("测试邮件-模板邮件");

        Map<String, Object> map = new HashMap<>();
        map.put("username", "校长");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        // 设定去哪里读取相应的ftl模板
        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
        // 在模板文件目录中寻找名称为name的模板文件
        Template template = cfg.getTemplate("email.ftl");

        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        helper.setText(html, true);

        sender.send(mimeMessage);
    }
}
