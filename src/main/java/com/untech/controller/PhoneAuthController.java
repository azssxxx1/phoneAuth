package com.untech.controller;

import com.untech.util.DesBase64Tool;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 三网手机号认证 、手机号在线时长
 * Created by sxy on 2017/5/12.
 */
public class PhoneAuthController {
    private static final Logger logger = Logger.getLogger(PhoneAuthController.class);

    private static final String url_auth = "http://apis.haoservice.com/efficient/cellphone";
    private static final String url_onlinTilme = "http://apis.haoservice.com/efficient/cellphone/onlineinterval";
    private static final String apikey_auth = "174a6032efc7432cbf40a57f07329075";
    private static final String apikey_onlinTilme = "d748806e6e174012a83824e3248baf9c";

    /**
     * 三网手机号实名认证接口
     *
     * @param idCard
     * @param mobile
     * @param realName
     * @return
     */
    public static String phoneAuth(String idCard, String mobile, String realName) {
        String reqStr = "mobile=" + mobile + "&idCard=" + idCard + "&realName=" + realName;
        logger.debug("实名认证参数：" + reqStr);
        String resStr = "";
        String VerificationResult = "";

        try {
            String params = DesBase64Tool.desEncrypt(reqStr);
            String url = url_auth + "?IsEncrypt=true&key=" + apikey_auth + "&params=" + params;

            String res = httpUtil(url);
            logger.debug(res);
            resStr = DesBase64Tool.desDecrypt(res);
//            {"error_code": 0, "reason": "成功","result":
//            { "Name": "张强","CardNo": "14012XXXXXXXXXX","Mobile": "13331033200","VerificationResult": "-1" }}
            logger.debug("返回结果：" + resStr);
            JSONObject resultObj = JSONObject.fromObject(resStr);
            String result = resultObj.getString("result");

            if (!"".equals(result) && result != null) {
                JSONObject jsonObject = JSONObject.fromObject(result);
                VerificationResult = jsonObject.getString("VerificationResult");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return VerificationResult;
    }

    /**
     * 手机号在线时长
     *
     * @param mobile
     * @return
     */
    public static String phoneOnlineTime(String mobile) {
        String reqStr = "mobile=" + mobile;
        logger.debug("实名认证参数：" + reqStr);
        String resStr = "";
        String Value = "";

        try {
            String params = DesBase64Tool.desEncrypt(reqStr);
            String url = url_onlinTilme + "?IsEncrypt=true&key=" + apikey_onlinTilme + "&params=" + params;

            String res = httpUtil(url);
            logger.debug("返回结果：" + res);
            resStr = DesBase64Tool.desDecrypt(res);
            logger.debug("返回结果：" + resStr);

            //{"error_code": 0, "reason": "成功","result": {"Mobile": "18003711222","QueryResult": "1","Value": "6-12"}}
            JSONObject resultObj = JSONObject.fromObject(resStr);
            String result = resultObj.getString("result");

            if (!"".equals(result) && result != null) {
                JSONObject jsonObject = JSONObject.fromObject(result);
                Value = jsonObject.getString("Value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Value;
    }

    public static String httpUtil(String url) {
        String result = "";
        try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(url);//这里发送get请求d
            // 获取当前客户端对象
            HttpClient httpClient = new DefaultHttpClient();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.getStatusLine().getStatusCode());

            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]) {
//        String jsonStr="{\"error_code\": 0, \"reason\": \"成功\"," +
//                "\"result\": {\"Mobile\": \"18003711222\",\"QueryResult\": \"1\",\"Value\": \"6-12\"}}";
//        String jsonStr = "{\"error_code\": 0, \"reason\": \"成功\",\"result\":{ \"Name\": \"张强\",\"CardNo\": \"14012XXXXXXXXXX\",\"Mobile\": \"13331033200\",\"VerificationResult\": \"-1\" }}";
//        JSONObject resultObj = JSONObject.fromObject(jsonStr);
//        String result = resultObj.getString("result");
//
//        if (!"".equals(result) && result != null) {
//            JSONObject jsonObject = JSONObject.fromObject(result);
//            String value = jsonObject.getString("VerificationResult");
//            System.out.println(value);
//        }
        String text = phoneAuth("340322198310083032","15255151981","陈吉友");
        System.out.println(text);
//        String str = phoneOnlineTime("18667935421");
//        System.out.println(str);
    }

}
