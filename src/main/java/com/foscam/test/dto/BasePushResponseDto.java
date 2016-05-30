package com.foscam.test.dto;

import org.foscam.core.dto.BaseResParamsDTO;
/**
 * 统一返回，适合所有
 * @author Administrator
 *
 */
public class BasePushResponseDto extends BaseResParamsDTO{

	
	private static final long serialVersionUID = -161315086367586467L;
	
	
	private  Object data = null;
	
	public BasePushResponseDto(){
		
	}
	
	public BasePushResponseDto(Object data){
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
		
}
