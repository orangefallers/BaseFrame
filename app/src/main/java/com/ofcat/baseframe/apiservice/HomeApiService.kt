package com.ofcat.baseframe.apiservice


import com.ofcat.baseframe.model.BaseDto
import com.ofcat.baseframe.model.ConfigDataDto
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HomeApiService {

    /**
     * @return Observable response data
     */
    @GET("{Api Path}")
    fun getConfig(): Observable<Response<BaseDto<ConfigDataDto>>>

    /**
     * @return Observable response data
     */
    @GET("{Api Path}")
    fun version(
        @Query("device_type") deviceType: Int
    ): Observable<Response<BaseDto<ConfigDataDto>>>

    /**
     * @return Observable response data
     */
    @GET("{Api Path}")
    fun search(
        @Header("token") uidHeader: String,
        @Query("device_type") keywords: String
    ): Observable<Response<BaseDto<ConfigDataDto>>>

}