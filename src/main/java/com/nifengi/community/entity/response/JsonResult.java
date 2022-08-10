package com.nifengi.community.entity.response;

import com.nifengi.community.exception.BaseException;
import com.nifengi.community.exception.ErrorEnum;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yu
 * @title: JsonResult
 * @projectName community
 * @date 2022/7/27 12:05
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1456468469856161L;

    /**
     * 返回状态码
     */
    private Integer status;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回内容
     */
    private T data;



    /**
     * 自定义返回状态码
     *
     * @param status
     * @param message
     * @param data
     * @return com.nifengi.www.util.JsonResult
     * @author Yu
     */
    private static <T> JsonResult<T> result(int status, String message, T data) {
//        JsonResult<T> result = new JsonResult<>();
//        result.status = status;
//        result.message = message;
//        result.data = data;
        return new JsonResult<>(status, message, data);
    }

    /**
     * 自定义返回状态码
     *
     * @param httpStatus
     * @param data
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    private static <T> JsonResult result(HttpStatus httpStatus, T data) {
        return result(httpStatus.status, httpStatus.value, data);
    }

    /**
     * 自定义返回状态码 [不建议使用，建议扩展方法]
     *
     * @param status
     * @param message
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult result(int status, String message) {
        return result(status, message, null);
    }

    /**
     * 自定义异常返回结果
     *
     * @param e
     * @return com.nifengi.www.util.JsonResult
     * @author* Yu
     * @date 2021/6/10
     */
    public static <T> JsonResult defineError(BaseException e) {
        JsonResult result = new JsonResult();
        result.status = e.getErrorCode();
        result.message = e.getErrorMsg();
        result.data = null;
        return result;
    }

    /**
     * 其他异常返回结果
     *
     * @param
     * @return com.nifengi.www.util.JsonResult
     * @author* Yu
     * @date 2021/6/10
     */
    public static <T> JsonResult otherError(ErrorEnum errorEnum) {
        JsonResult result = new JsonResult();
        result.status = errorEnum.getErrorCode();
        result.message = errorEnum.getErrorMsg();
        result.data = null;
        return result;
    }


    /**
     * 操作成功
     *
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult success() {
        return success(HttpStatus.SUCCESS.value, null);
    }

    /**
     * 操作成功
     *
     * @param totalPage
     * @param total
     * @param list
     * @author Yu
     */
    public static <T> JsonResult success(int totalPage, long total, List<T> list) {
        return success(new ResultPage<T>(totalPage, total, list));
    }


    /**
     * 操作成功
     *
     * @param total
     * @param list
     * @author Yu
     */
    public static <T> JsonResult success(long total, List<T> list) {
        return success(new ResultPage<T>(list.size(), total, list));
    }

    /**
     * 操作成功
     *
     * @param resultPage
     * @author Yu
     */
    public static <T> JsonResult success(ResultPage<T> resultPage) {
        return success(HttpStatus.SUCCESS.value, resultPage);
    }


    /**
     * 操作成功
     *
     * @param message
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult success(String message) {
        return success(message, null);
    }

    /**
     * 操作成功
     *
     * @param data
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult success(T data) {
        return success(HttpStatus.SUCCESS.value, data);
    }

    /**
     * 操作成功
     *
     * @param message
     * @param data
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult success(String message, T data) {
        return result(HttpStatus.SUCCESS.status, message, data);
    }


    /**
     * 操作失败
     *
     * @param message
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult fail(String message) {
        return result(HttpStatus.FAIL.status, message, null);
    }


    /**
     * 未授权
     *
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult error401() {
        return result(HttpStatus.UNAUTHORIZED.status, HttpStatus.UNAUTHORIZED.value, null);
    }


    /**
     * 不支持的请求类型
     *
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult error415() {
        return result(HttpStatus.UNSUPPORTED_MEDIA_TYPE.status, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value, null);
    }


    /**
     * 404 未找到
     *
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult error404() {
        return result(HttpStatus.NOT_FOUND.status, HttpStatus.NOT_FOUND.value, null);
    }


    /**
     * 服务器内部错误
     *
     * @return com.nifengi.wwww.util.JsonResult
     * @author Yu
     */
    public static <T> JsonResult error500() {
        return result(HttpStatus.INTERNAL_SERVER_ERROR.status, HttpStatus.INTERNAL_SERVER_ERROR.value, null);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResultPage<T> {

        /**
         * 当前页，有多少条
         */
        private Integer totalPage;

        /**
         * 总共，有多少条
         */
        private Long total;

        /**
         * 数据
         */
        private List<T> list;

    }

    /**
     * restful api 返回状态码。不够的时候自己扩展
     *
     * @author Yu
     * @return com.nifengi.wwww.util.JsonResult
     */
    @Getter
    public enum HttpStatus {

        /**
         * 正常 返回代码
         *
         * @author Yu
         */
        SUCCESS(200, "操作成功"),

        /**
         * 错误 返回代码
         *
         * @author Yu
         */
        FAIL(100, "操作失败"),

        /**
         * 参数检验失败 返回代码
         *
         * @author Yu
         */
        VALIDATE_FAILED(304, "参数检验失败"),

        /**
         * 404 为找到
         *
         * @author Yu
         */
        NOT_FOUND(404, "Not Found"),

        /**
         * 未经授权
         *
         * @author Yu
         */
        UNAUTHORIZED(401, "你还没有经过授权认证"),

        /**
         * 不支持的媒体类型
         *
         * @author Yu
         */
        UNSUPPORTED_MEDIA_TYPE(415, "不支持此请求类型！"),


        /**
         * 服务器内部错误 返回代码
         *
         * @author Yu
         */
        INTERNAL_SERVER_ERROR(500, "服务器内部错误");

        private int status;

        private String value;

        HttpStatus(int status, String value) {
            this.status = status;
            this.value = value;
        }

    }

}
