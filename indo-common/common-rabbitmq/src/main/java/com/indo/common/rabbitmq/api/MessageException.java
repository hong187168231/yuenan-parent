package com.indo.common.rabbitmq.api;

public class MessageException  extends  Exception{


    private static final long serialVersionUID = -8283764568495174322L;

    public MessageException(){
        super();
    }

    public MessageException(String message){
        super(message);
    }

    public MessageException(String message, Throwable cause){
        super(message, cause);
    }

    public MessageException( Throwable cause){
        super( cause);
    }
}