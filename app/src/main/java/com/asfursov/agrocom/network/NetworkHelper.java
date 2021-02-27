package com.asfursov.agrocom.network;

import android.text.Editable;
import android.util.Log;

import com.asfursov.agrocom.BuildConfig;
import com.asfursov.agrocom.model.UserData;
import com.asfursov.agrocom.settings.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {
    private static NetworkHelper mInstance;

    public static NetworkHelper getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkHelper();
        }
        return mInstance;
    }
    private Retrofit mRetrofit;

    private NetworkHelper() {

        OkHttpClient okHttpClient = getOkHttpClient();
        try {
            Gson gson =  new GsonBuilder().serializeNulls().create();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Settings.getInstance().getDBUrl())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        catch(Throwable e){
            Log.wtf("Retrofit error",e);
        }
    }

    public OkHttpClient getOkHttpClient() {

        return new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic(Settings.getInstance().getDBUser(),Settings.getInstance().getDBPassword()));

                Request newRequest = builder.build();
                okhttp3.Response response = chain.proceed(newRequest);

                return response;
            }
        }).connectTimeout(BuildConfig.CONNECT_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(BuildConfig.READ_TIMEOUT,TimeUnit.MINUTES)
                .writeTimeout(BuildConfig.WRITE_TIMEOUT,TimeUnit.MINUTES)
                .build();
    }

//    public static String getMainItemImageUrl(Item item) {
//        if(item==null) return "";
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(Settings.getInstance().getDBUrl())
//                .append("image?ItemId=")
//                .append(item.getId())
//                .append("&Content=2");
//
//        return sb.toString();
//    }

//    public static String getItemImageUrl(ItemImageData itemImageData) {
//        if(itemImageData==null) return "";
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(Settings.getInstance().getDBUrl())
//                .append("image?Id=")
//                .append(itemImageData.getId())
//                .append("&Content=2");
//
//        return sb.toString();
//    }

    public API  getAPI() {
        if (mRetrofit != null)
            return mRetrofit.create(API.class);
        return null;
    }
//
//    public Call<Item> invokeItemSearch(String template, KindOfItemSearch kindOfItemSearch) {
//        if (kindOfItemSearch==KindOfItemSearch.BARCODE)
//            return getAPI().ItemByBarcode((template.length()==12?"0":"")+template);
//        if (kindOfItemSearch==KindOfItemSearch.ITEM_ID)
//            return getAPI().ItemById(template);
//        return null;
//    }
}
