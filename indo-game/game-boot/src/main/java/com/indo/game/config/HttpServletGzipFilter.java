package com.indo.game.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "httpServletGzipFilter",urlPatterns = "/")
public class HttpServletGzipFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest)request),response);
    }
    @Override
    public void destroy() {
    }
}
