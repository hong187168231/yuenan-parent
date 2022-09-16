package com.indo.game.config;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
    private HttpServletRequest request;
    public HttpServletRequestWrapper(HttpServletRequest request){
        super(request);
        this.request = request;
    }
    @Override
    public ServletInputStream getInputStream()throws IOException{
        ServletInputStream stream = request.getInputStream();
        String contentEncoding = request.getHeader("content-encoding");
        if(null!=contentEncoding && contentEncoding.indexOf("gzip") != -1){
            try {
                final GZIPInputStream gzipInputStream = new GZIPInputStream(stream);
                ServletInputStream newStream = new ServletInputStream(){
                    @Override
                    public int read()throws  IOException{
                        return gzipInputStream.read();
                    }
                    @Override
                    public boolean isFinished(){
                        return false;
                    }
                    @Override
                    public boolean isReady(){
                        return false;
                    }
                    @Override
                    public void setReadListener(ReadListener readListener){

                    }
                };
                return newStream;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return stream;
    }
}
