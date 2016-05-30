package com.foscam.test.controller;

import org.apache.commons.lang.StringUtils;
import org.foscam.httpserver.annotation.HandlerModule;
import org.foscam.httpserver.annotation.RequestURI;
import org.foscam.httpserver.type.RequestMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.foscam.test.dto.BasePushResponseDto;

@Component
@HandlerModule("test")
public class TestController {
	
	private Logger log = LoggerFactory.getLogger(TestController.class);

	
	@RequestURI(value = "/index", method = RequestMode.GET)
	public Object checkBindingByCloudAccount(String username ) {
		
		BasePushResponseDto dto = new BasePushResponseDto();
		try {
			//参数校验
			log.info(" welcome to test index...., username = {} ",username);
			if(StringUtils.isNotEmpty(username)&&username.length()>5){
				throw new IllegalArgumentException();
			}
			dto.setData("");
			return  dto;
		}catch (Exception e){
			log.error(e.getMessage(),e);
			dto.setErrorCode("000099");
			dto.setFailureDetails(e.getMessage());
		}
		
		return dto;
	}
	
	
}
