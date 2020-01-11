package com.mobbile.paul.provider

import com.mobbile.paul.model.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {

    @Headers("Connection:close")
    @POST("/api/rep_logins")
    fun Login(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("imei") imei: String
    ): Single<Response<ApplicationLogin>>

    @Headers("Connection:close")
    @POST("/api/rep_customers")
    fun getAllCustomers(
        @Query("repid") repid: Int
    ): Single<Response<CustomersRemoteResponse>>

    @Headers("Connection:close")
    @POST("api/rep_sales_daily_entry")
    fun getBasket(
        @Query("customerno") customerno: String,
        @Query("customer_code") customer_code: String,
        @Query("repid") repid: Int
    ): Single<Response<InitBasket>>

    @Headers("Connection:close")
    @POST("/api/tmrepbasket")
    fun getCustomerNo(
        @Query("customerno") customerno: String
    ): Single<Response<Basket>>

    @Headers("Connection:close")
    @POST("/api/rep_daily_attendant")
    fun takeAttendant(
        @Query("taskid") taskid: Int,
        @Query("repid") repid: Int,
        @Query("outletlat") outletlat: Double,
        @Query("outletlng") outletlng: Double,
        @Query("currentLat") currentLat: Double,
        @Query("currentLng") currentLng: Double,
        @Query("distance") distance: String,
        @Query("duration") duration: String,
        @Query("sequenceno") sequenceno: String,
        @Query("arrivaltime") arrivaltime: String
    ): Single<Response<Attendant>>

    @Headers("Connection:close")
    @POST("/api/rep_close_outlet_on_sales")
    fun CloseOutlets(
        @Query("repid") repid: Int,
        @Query("currentlat") currentlat: String,
        @Query("currentlng") currentlng: String,
        @Query("outletlat") outletlat: String,
        @Query("outletlng") outletlng: String,
        @Query("arrivaltime") arrivaltime: String,
        @Query("visitsequence") visitsequence: Int,
        @Query("distance") distance: String,
        @Query("duration") duration: String,
        @Query("urno") urno: Int
    ): Single<Response<Attendant>>

    @Headers("Connection:close")
    @POST("/api/rep_open_outlet_on_sales")
    fun fetchPostSales(
        @Body datas: postToServer
    ): Single<Response<Attendant>>

}