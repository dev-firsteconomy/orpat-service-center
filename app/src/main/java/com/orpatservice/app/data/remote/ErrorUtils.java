package com.orpatservice.app.data.remote;

import com.google.gson.Gson;

import okhttp3.ResponseBody;

/**
 * Created by Vikas Singh on 13/Dec/2020
 */


public class ErrorUtils {
    private static final int UNKNOWN_ERROR = 10001;
    private static final String UNKNOWN_ERROR_MESSAGE = "unknown_error";

    public static Error getError(ResponseBody responseBody , int code){
        try {
            final String errorBody = responseBody.string();
            final Error error = new Gson().fromJson(errorBody,Error.class);
            error.setCode(code);
            return error;
        } catch (Exception e) {
            e.printStackTrace();
            return getError(e);
        }
    }

    public static Error getError(Throwable throwable) {
        return new Error(throwable.getMessage() !=null ? throwable.getMessage() : UNKNOWN_ERROR_MESSAGE,UNKNOWN_ERROR);
    }
}