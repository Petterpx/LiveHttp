package com.cloudx.core.factory;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Petterp
 * on 2019-12-21
 * Function:
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type mType;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        if (mType == String.class || mType == RequestBody.class) {
            return (T) response;
        }
        return gson.fromJson(response, mType);
    }
}
