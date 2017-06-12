package com.hengda.smart.changsha.d.http;

/**
 * Created by baishiwei on 2016/4/28.
 * 请求失败的错误处理，在这里添加自定义的错误消息类型
 */
public class ApiException extends RuntimeException {

    public static final int ErrorType = 100;

    public ApiException(int resultCode, String msg) {
        this(getApiExceptionMessage(resultCode, msg));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code, String msg) {
        String message = "";
        switch (code) {
            case ErrorType:
                break;
            default:
                message = msg;
        }
        return message;
    }
}

