package com.user.brayan.pruebatecnica.Remote

import retrofit2.create

object ApiUtils {
    var url = "http://ws4.shareservice.co/TestMobile/rest/"

    fun getService() : UserService {
        return RetrofitClient.getClient(url).create(UserService::class.java)
    }
}