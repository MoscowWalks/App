package com.bashalex.cityquest;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Alex Bash on 08.10.16.
 */

public class API {
    private static final String TAG = "API";

    private static CityQuestService CityQuestClient;
    private static final String URL = "http://moscowwalks6473.cloudapp.net:8000/";

    private static Double location_latitude, location_longitude;
    private static Double destination_latitude, destination_longitude;
    private static Integer arrivalHour, arrivalMinute;
    private static String destinationName;
    public static int objectNum = -1;

    public static void saveParams(Double location_latitude, Double location_longitude,
                                  Double destination_latitude, Double destination_longitude,
                                  Integer arrivalHour, Integer arrivalMinute,
                                  String destinationName) {
        API.location_latitude = location_latitude;
        API.location_longitude = location_longitude;
        API.destination_latitude = destination_latitude;
        API.destination_longitude = destination_longitude;
        API.arrivalHour = arrivalHour;
        API.arrivalMinute = arrivalMinute;
        API.destinationName = destinationName;
    }

    private static OkHttpClient getLogger() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    private static CityQuestService getClient() {
        if (CityQuestClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(getLogger())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            CityQuestClient = retrofit.create(CityQuestService.class);
        }
        return CityQuestClient;
    }

    public static Observable<CityQuestResponse> getRoute(boolean flag) {
        if (flag) {
            API.objectNum = 0;
        } else {
            ++API.objectNum;
        }
        return getClient().getRoute(API.location_latitude, API.location_longitude,
                                    API.destination_latitude, API.destination_longitude,
                                    API.arrivalHour, API.arrivalMinute, API.objectNum,
                                    API.destinationName)
                .onErrorReturn(CityQuestResponse::new)
                .doOnError(API::handleError);
    }

    private synchronized static void handleError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException ||
                throwable instanceof UnknownHostException) {
        } else throwable.printStackTrace();
    }
}
