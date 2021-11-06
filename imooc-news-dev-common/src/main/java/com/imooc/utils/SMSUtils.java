package com.imooc.utils;

import org.springframework.stereotype.Component;

import java.rmi.ServerException;

@Component
public class SMSUtils {

    public void sendSMS(String mobile, String code) {
//        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
//                                                aliyunResource.getAccessKeyID(),
//                                                aliyunResource.getAccessKeySecret());
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        CommonRequest request = new CommonRequest();
//        request.setSysMethod(MethodType.POST);
//        request.setSysDomain("dysmsapi.aliyuncs.com");
//        request.setSysVersion("2017-05-25");
//        request.setSysAction("SendSms");
//        request.putQueryParameter("RegionId", "cn-hangzhou");
//
//        request.putQueryParameter("PhoneNumbers", mobile);
//        request.putQueryParameter("SignName", "风间影月");
//        request.putQueryParameter("TemplateCode", "SMS_183761535");
//        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
//
//        try {
//            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
    }


}
