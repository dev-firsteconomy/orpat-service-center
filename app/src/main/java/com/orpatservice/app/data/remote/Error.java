package com.orpatservice.app.data.remote;

/**
 * Created by Vikas Singh on 13/Dec/2020
 */

public class Error {
    private String message;
    private int code;

    public Error() {
        // no-op
    }

    public Error(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Code: " + code
                + " Message: " + message;
    }
}