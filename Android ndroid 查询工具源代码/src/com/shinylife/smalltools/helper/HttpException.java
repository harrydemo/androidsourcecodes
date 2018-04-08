package com.shinylife.smalltools.helper;

public class HttpException extends Exception {
    private int statusCode = -1;
    private static final long serialVersionUID = -2623309261327598087L;

    public HttpException(String msg) {
        super(msg);
    }

    public HttpException(Exception cause) {
        super(cause);
    }

    public HttpException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;

    }

    public HttpException(String msg, Exception cause) {
        super(msg, cause);
    }

    public HttpException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;

    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
