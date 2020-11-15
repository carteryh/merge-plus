package com.common.dto;

import com.common.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称：spring-cloud-service
 * 类 名 称：RestResponse
 * 类 描 述：响应
 * 创建时间：2020/8/11 2:25 下午
 * @author chenyouhong
 */
@Data
public class RestResponse implements Serializable {

    private int code;
    private String message;
    private Object data;

    public RestResponse() {
    }

    public RestResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RestResponse success(Object data) {
        return new RestResponse(1, "success", data);
    }

    public static RestResponse error(String msg) {
        return new RestResponse(0, msg, null);
    }

//    /**
//     * 入参校验失败
//     *
//     * @param errorMsg
//     * @return
//     */
//    public static RestResponse errorByParamsVerifyFail(String errorMsg) {
//        RestResponse t = new RestResponse();
//        t.setCode(ResultEnum.PARAMS_VERIFY_FAIL.getCode());
//        t.setMessage(errorMsg);
//        return t;
//    }
//
//    /**
//     * 自定义code异常，请确认code在@see ResultEnum中
//     *
//     * @param code     自定义的code
//     * @param errorMsg 错误消息
//     * @return 结果
//     */
//    public static Result errorByCode(int code, String errorMsg) {
//        Result t = new Result<>();
//        t.setCode(code);
//        t.setMsg(errorMsg);
//        return t;
//    }
//
//    /**
//     * 业务处理异常
//     *
//     * @param errorMsg
//     * @return
//     */
//    public static Result errorByBus(String errorMsg) {
//        Result t = new Result<>();
//        t.setCode(ResultEnum.BUS_ERROR.getCode());
//        t.setMsg(errorMsg);
//        return t;
//    }
//
//    /**
//     * 系统异常
//     *
//     * @param errorMsg
//     * @return
//     */
//    public static Result errorBySys(String errorMsg) {
//        Result t = new Result<>();
//        t.setCode(ResultEnum.SYS_ERROR.getCode());
//        t.setMsg(errorMsg);
//        return t;
//    }
//
//    /**
//     * 失败
//     *
//     * @param errorMsg 失败信息
//     * @return
//     */
//    public static Result fail(String errorMsg) {
//        Result t = new Result<>();
//        t.setCode(ResultEnum.FAIL.getCode());
//        t.setMsg(errorMsg);
//        return t;
//    }
//
//    /**
//     * 不带参数的成功
//     *
//     * @return
//     */
//    public static Result success() {
//        return success(null);
//    }
//
//    /**
//     * 成功
//     *
//     * @param data 要返回的数据
//     * @param <T>  要返回的类型
//     * @return
//     */
//    public static <T> Result<T> success(T data) {
//        Result<T> t = new Result<>();
//        t.setData(data);
//        t.setCode(ResultEnum.SUCCESS.getCode());
//        return t;
//    }

}
