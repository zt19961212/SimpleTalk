package com.william.common;


public class Common {
    /**
     * 一些不可变的参数
     */
    public interface Constance {
        // 手机号的正则,11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        // 基础的网络请求地址
        String API_URL = "http://120.24.0.174:8888/SimpleTalk/api/";

        // 最大的上传图片大小860kb
        long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;
        int REQUEST_FOLLOW=220;
        int RESULT_FOLLOW=230;
        String TAG_GROUPMEMBERADD ="GROUPMEMBERADD";
        String TAG_GROUPJOIN="GROUPJOIN";
    }
}
