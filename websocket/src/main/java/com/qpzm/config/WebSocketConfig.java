package com.qpzm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**  
* @Description: websocket配置类
* @Author: 风起于青苹之末
* @Date: 2021年4月7日
*/
@Configuration
public class WebSocketConfig {
	
	//用于扫描带有@ServerEndpoint的注解成为websocket,如果你使用外置的tomcat就不需要该配置文件
	@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
