package com.catlbattery.alm;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {  
    private String encoding = "utf-8";  
  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {  
        //设置request编码  
        request.setCharacterEncoding(encoding);  
        //设置response编码  
        response.setContentType("text/html;charset=" + encoding);  
        response.setCharacterEncoding(encoding);  
        filterChain.doFilter(request, response);         
    }  
  
    public void init(FilterConfig filterConfig) throws ServletException {  
        String encodingParam = filterConfig.getInitParameter("encoding");  
        if (encodingParam != null) {  
            encoding = encodingParam;  
        }  
    }  
  
    public void destroy() {  
          
    }  
}  