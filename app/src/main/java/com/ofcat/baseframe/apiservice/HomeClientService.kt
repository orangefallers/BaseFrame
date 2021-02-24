package com.ofcat.baseframe.apiservice

import com.ofcat.baseframe.model.BaseDto
import com.ofcat.baseframe.model.ConfigDataDto
import io.reactivex.Observable

open class HomeClientService : BaseClientService() {

    /**
     * Singleton
     */
    companion object {
        val instance: HomeClientService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HomeClientService()
        }
    }

    /**
     * Retrofit api service
     */
    private val service: HomeApiService =
        applyClientService("{API Url}").create(HomeApiService::class.java)

    /**
     * @return Observable response data
     */
    open fun getConfig(): Observable<BaseDto<ConfigDataDto>> {
        return service.getConfig().compose(applySubscribeVo())
    }

}