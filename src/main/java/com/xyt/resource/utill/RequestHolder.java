package com.xyt.resource.utill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class RequestHolder {
    public static HttpServletRequest getRequest(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return req;
    }

    public static HttpServletResponse getResponse(){
        HttpServletResponse resp = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        return resp;
    }

    public static HttpSession getSession(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
    }

    public static HttpSession getSession(boolean create){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(create);
    }
    public static String getHost(){
		 HttpServletRequest request =  getRequest();
        String serverName = request.getServerName();
        String scheme = request.getScheme();
        // + ":" + request.getServerPort()
        String host = scheme + "://" + serverName;
        return host;
	 }

    /**
     * 获取URL请求的端口
     * @return
     */
    public static int getServerPort() {
        HttpServletRequest request = getRequest();
        int port = request.getServerPort();
        return port;
    }
}
