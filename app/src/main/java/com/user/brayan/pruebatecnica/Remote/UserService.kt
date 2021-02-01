package com.user.brayan.pruebatecnica.Remote

import com.user.brayan.pruebatecnica.Model.Products
import com.user.brayan.pruebatecnica.Model.ResObj
import com.user.brayan.pruebatecnica.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @POST("Login")
    @Headers("Content-Type: application/json")
    fun login(@Body user : User) : Call<ResObj>

    @POST("GetProductsData")
    @Headers("Content-Type: application/json")
    fun productsData() : Call<ArrayList<Products>>
}