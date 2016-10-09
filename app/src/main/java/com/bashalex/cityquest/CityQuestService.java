package com.bashalex.cityquest;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Alex Bash on 08.10.16.
 */

interface CityQuestService {
    @GET("/route")
    Observable<CityQuestResponse> getRoute(@Query("location1") Double location1,
                                           @Query("location2") Double location2,
                                           @Query("destination1") Double destination1,
                                           @Query("destination2") Double destination2,
                                           @Query("arrival_hour") Integer arrivalHour,
                                           @Query("arrival_minute") Integer arrivalMinute,
                                           @Query("step") Integer step,
                                           @Query("name") String name);
}
