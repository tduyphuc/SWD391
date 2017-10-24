package com.hotel.phuctdse61834.hotelbooking.request;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johntran on 10/14/17.
 */

public class Requester {
    private static final String SCHEME = "http";
    private static final String HOST = "192.168.43.115";
    private static final int PORT= 8080;
    private static final String MAIN_PATH= "SpringWebService";

    private Gson gson = new Gson();

    private HttpUrl.Builder getMainBuilder(){
        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme(SCHEME).host(HOST).port(PORT).addPathSegment(MAIN_PATH);
        return builder;
    }

    private HttpUrl getFullUrl(List<String> reqPath, Map<String, String> params){
        HttpUrl.Builder builder = getMainBuilder();
        for (String path : reqPath){
            builder.addPathSegment(path);
        }
        if(params != null){
            for(String key : params.keySet()){
                builder.addQueryParameter(key, params.get(key));
            }
        }
        return builder.build();
    }

    private RequestBody getFormRequestBody(Map<String, String> params){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for(String key : params.keySet()){
            builder.add(key, params.get(key));
        }
        return builder.build();
    }

    public Response postReq(List<String> reqPath, Map<String, String> params) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = getFullUrl(reqPath, null);
        RequestBody body = getFormRequestBody(params);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            return response;
        }
        return null;
    }

    public Response getReq(List<String> reqPath, Map<String, String> params) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = getFullUrl(reqPath, params);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            return response;
        }
        return null;
    }

}
