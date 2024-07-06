package com.example.personalcoffee;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.CookieManager;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Net {

    private static Net ourinstance = new Net();
    public final static String URL = "http://localhost:3000";
    private static ApiService apiService;

    public static Net getInstance(){
        return ourinstance;
    }


    private Net(){
    }

    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    CookieJar cookieJar = new JavaNetCookieJar(new CookieManager());
    OkHttpClient okHttpClient = builder
            .cookieJar(cookieJar)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ApiService getApiService(){
        if(apiService == null){
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
