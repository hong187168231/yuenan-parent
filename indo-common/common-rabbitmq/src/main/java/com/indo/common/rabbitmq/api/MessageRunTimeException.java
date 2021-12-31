package com.indo.common.rabbitmq.api;

public class MessageRunTimeException extends  RuntimeException{


    private static final long serialVersionUID = -2591307228826723236L;

    public MessageRunTimeException(){
        super();
    }

    public MessageRunTimeException(String message){
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause){
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause){
        super( cause);
    }
}