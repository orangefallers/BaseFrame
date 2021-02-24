package com.ofcat.baseframe.apiservice

import com.ofcat.baseframe.model.BaseDto
import com.ofcat.baseframe.model.ConfigDataDto
import io.reactivex.Observable

class HomeMockService : HomeClientService() {

    override fun getConfig(): Observable<BaseDto<ConfigDataDto>> {

        val baseVo = BaseDto<ConfigDataDto>()
        baseVo.data = ConfigDataDto()
        baseVo.code = "0"

        return Observable.just(baseVo)
    }

}