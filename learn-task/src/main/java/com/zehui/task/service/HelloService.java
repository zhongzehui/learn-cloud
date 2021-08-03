package com.zehui.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class HelloService {

    @Autowired
    private JavaMailSender mailSender;

    @Async//开启异步功能
    public String helloAsync() {
        try {
            // TimeUnit.SECONDS.sleep(3);
            TimeUnit.MILLISECONDS.sleep(3);
            System.out.println("HelloService输出结果 休眠结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "执行完成";
    }

    public boolean sendMail() {
        if (mailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) mailSender;
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(javaMailSender.getUsername());
            mailMessage.setTo("380569013@qq.com");
            mailMessage.setSubject("Testing mail");
            mailMessage.setText("测试谷歌邮箱");
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                e.printStackTrace();
                System.out.println(e.getStackTrace());
                return false;
            } finally {
            }
        }
        return true;

    }
}
