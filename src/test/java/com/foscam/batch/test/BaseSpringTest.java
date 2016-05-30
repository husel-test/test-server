package com.foscam.batch.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml" })
public class BaseSpringTest extends AbstractJUnit4SpringContextTests{
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected String className = this.getClass().getName();
	
	@Before
	public void beforeTest(){
		//log.warn(">>>---{}  test start ------------>>>",className);
		System.err.println("---"+className+"  test start------------>>>");
		
	}
	@After
	public void afterTest(){
		System.err.println("<<<---"+className+"  test end  -----------<<<");
		//log.warn("<<<---{}  test end  -----------<<<",className);
	}
}
