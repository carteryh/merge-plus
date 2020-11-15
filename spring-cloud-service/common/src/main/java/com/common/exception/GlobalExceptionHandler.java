//package com.common.exception;
//
//import cn.hutool.core.exceptions.ValidateException;
//import cn.hutool.core.util.StrUtil;
//import com.common.dto.RestResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
///**
// * 项目名称：spring-cloud-service
// * 类 名 称：GlobalExceptionHandler
// * 类 描 述：global exception
// * 创建时间：2020/8/11 11:15 上午
// * 创 建 人：chenyouhong
// */
//@ResponseBody
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    /**
//     * 如果抛出的是参数异常，将其当做业务异常处理
//     *
//     * @return
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.OK)
//    RestResponse handlerMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException re) {
//        StringBuilder sb = new StringBuilder();
//
//        re.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            sb.append("[").append(fieldName).append("]").append(error.getDefaultMessage()).append(";    ");
//        });
//        String str = StrUtil.format("参数未通过校验:{}", sb.toString());
//        return RestResponse.error(str);
//    }
//
////    @ResponseStatus(HttpStatus.OK)
////    @ExceptionHandler(BindException.class)
////    public RestResponse handlerBindException(BindException e) {
////        BindingResult bindingResult = e.getBindingResult();
////
////        // 所有参数异常信息
////        List<ObjectError> allErrors = bindingResult.getAllErrors();
////
////        return RestResponse.errorByParamsVerifyFail(allErrors.get(0).getDefaultMessage());
////    }
//
////
////    /**
////     * 如果抛出的是分布式锁异常，将其当做业务异常处理
////     *
////     * @return
////     */
////    @ExceptionHandler(DistributedLockedException.class)
////    @ResponseStatus(HttpStatus.OK)
////    Result distributedLockedException(HttpServletRequest request, DistributedLockedException re) {
////        return Result.errorByCode(ResultEnum.DISTRIBUTED_LOCKED.getCode(), re.getMessage());
////    }
////
////
////    /**
////     * 如果抛出的是带状态码的异常，将其当做业务异常处理
////     *
////     * @return
////     */
////    @ExceptionHandler(StatefulException.class)
////    @ResponseStatus(HttpStatus.OK)
////    Result handlerState(HttpServletRequest request, StatefulException re) {
////        return Result.errorByCode(re.getStatus(), re.getMessage());
////    }
//
//
//    /**
//     * 如果抛出的是校验异常，将其当做业务异常处理
//     *
//     * @return
//     */
////    @ExceptionHandler(ValidateException.class)
////    @ResponseStatus(HttpStatus.OK)
////    RestResponse handlerBusError(HttpServletRequest request, ValidateException re) {
////        return RestResponse.errorByBus(re.getMessage());
////    }
//
//    /**
//     * 如果抛出的是Exception 指定为系统异常
//     *
//     * @return
//     */
////    @ExceptionHandler(Exception.class)
////    @ResponseStatus(HttpStatus.BAD_REQUEST)
////    Result handleException(Exception e) {
////        logger.error("系统异常,请稍后重试!详细错误信息为：{}", e.getMessage(), e);
////        return Result.errorBySys("系统异常,请稍后重试!详细错误信息为：" + e.getMessage());
////    }
//
//}
