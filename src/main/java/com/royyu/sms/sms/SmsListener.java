package com.royyu.sms.sms;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.royyu.sms.util.SmsUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @JmsListener(destination = "itcast_sms_queue")
    public void receiveSmsMessage(Map<String, String> map){

        try {
            //发送短信
            SendSmsResponse response = smsUtil.sendSms(map.get("mobile"),
                    map.get("signName"), map.get("templateCode"), map.get("templateParam"));

            System.out.println("短信接口返回的数据----------------");
            System.out.println("Code=" + response.getCode());
            System.out.println("Message=" + response.getMessage());
            System.out.println("RequestId=" + response.getRequestId());
            System.out.println("BizId=" + response.getBizId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "123")
    public void querySendDetails(Map<String, String> map){

        try {
            //查询已发送短信
            QuerySendDetailsResponse querySendDetailsResponse = smsUtil.querySendDetails("16619862603", "");

            System.out.println("短信接口返回的数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
