package com.common.enums;

import lombok.Getter;

/**
 * 系统来源枚举
 * 
 * @author chenyouhong
 *
 */
@Getter
public enum ResultEnum {

	/**
	 * 成功
	 */
	SUCCESS(1, "成功"),
	/**
	 * 失败
	 */
	FAIL(0, "失败"),

	ENUM_CODE_NOT_EXIST(2001, "对应的枚举不存在"),
	/**
	 * 系统异常
	 */
	SYS_ERROR(2100, "系统异常"),
	/**
	 * 业务处理异常
	 */
	BUS_ERROR(2200, "业务处理异常"),
	/**
	 * 分布式锁被抢占异常
	 */
	DISTRIBUTED_LOCKED(2201, "分布式锁已被抢占"),
	/**
	 * 入参校验失败
	 */
	PARAMS_VERIFY_FAIL(2300, "入参校验未通过");

	private Integer code;

	private String msg;

	ResultEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
