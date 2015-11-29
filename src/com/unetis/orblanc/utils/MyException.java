package com.unetis.orblanc.utils;

public class MyException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MyException () {

    }

    public MyException (String message) {
        super (message);
    }

    public MyException (Throwable cause) {
        super (cause);
    }

    public MyException (String message, Throwable cause) {
        super (message, cause);
    }
    
    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
    	super(message, cause, enableSuppression, writableStackTrace);
    }
}
