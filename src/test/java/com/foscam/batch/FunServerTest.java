package com.foscam.batch;

import java.io.File;
import org.foscam.container.server.FunServer;

public class FunServerTest {

	/**
	 * 启动spring容器
	 */
	public static void main(String[] args) throws Exception {
		FunServer funServer = new FunServer();
		String dir = System.getProperty("user.dir") + File.separator + "target"
				+ File.separator + "classes";
		System.setProperty("funserver.home", dir);
		funServer.start(null);
	}
}