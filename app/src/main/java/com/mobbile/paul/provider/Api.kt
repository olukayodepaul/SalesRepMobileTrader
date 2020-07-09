package com.mobbile.paul.provider

import com.mobbile.paul.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface Api {

    //rep login
    //van login
    /*@Headers("Connection:close")
    @POST("/api/vanlogin")
    fun Login(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("imei") imei: String
    ): Single<Response<ApplicationLogin>>*/
//tokenlogin
    @Headers("Connection:close")
    @POST("/api/customer/userslogins")
    fun Login(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("imei") imei: String,
        @Query("devicetoken") devicetoken: String
    ): Single<Response<ApplicationLogin>>

    @Headers("Connection:close")
    @POST("/api/rep_customers")
    fun getAllCustomers(
        @Query("repid") repid: Int
    ): Single<Response<CustomersRemoteResponse>>

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
    @POST("api/rep_sales_daily_entry")
    fun getBasket(
        @Query("customerno") customerno: String,
        @Query("customer_code") customer_code: String,
        @Query("repid") repid: Int
    ): Single<Response<InitBasket>>


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
        @Query("urno") urno: Int,
        @Query("uiid") uiid: String
    ): Single<Response<Attendant>>

    @Headers("Connection:close")
    @POST("/api/rep_open_outlet_on_sales")
    fun fetchPostSales(
        @Body datas: postToServer
    ): Single<Response<Attendant>>


    @Headers("Connection:close")
    @POST("/api/rep_sales_details")
    fun getSalesDetails(
        @Query("customer_code") customer_code: String
    ): Single<Response<SalesDetails>>

    @Headers("Connection:close")
    @POST("/api/rep_sales_details")
    fun getBankDetails(
        @Query("customer_code") customer_code: String
    ): Single<Response<BankDetails>>

    @Headers("Connection:close")
    @POST("/api/rep_sales_details_for_each_outlet")
    fun getDetailsForEachSales(
        @Query("rep_id") rep_id: Int,
        @Query("urno") urno: Int
    ): Single<Response<DetailsForEachSales>>

    @Headers("Connection:close")
    @POST("/api/tm_update_outlet")
    fun updateOutlet(
        @Query("tmid") tmid: Int,
        @Query("urno") urno: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("outletname") outletname: String,
        @Query("contactname") contactname: String,
        @Query("outletaddress") outletaddress: String,
        @Query("contactphone") contactphone: String,
        @Query("outletclassid") outletclassid: Int,
        @Query("outletlanguageid") outletlanguageid: Int,
        @Query("outlettypeid") outlettypeid: Int
    ): Single<Response<OutletUpdateResponse>>

    @Headers("Connection:close")
    @POST("/api/tm_map_outlet")
    fun mapOutlet(
        @Query("repid") repid: Int,
        @Query("tmid") tmid: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("outletname") outletname: String,
        @Query("contactname") contactname: String,
        @Query("outletaddress") outletaddress: String,
        @Query("contactphone") contactphone: String,
        @Query("outletclassid") outletclassid: Int,
        @Query("outletlanguageid") outletlanguageid: Int,
        @Query("outlettypeid") outlettypeid: Int
    ): Single<Response<OutletUpdateResponse>>

    @Headers("Connection:close")
    @POST("/api/tm_outlet_info_async")
    fun CustometInfoAsync(
        @Query("urno") urno: Int
    ): Single<Response<OutletAsyn>>

    @Headers("Connection:close")
    @POST("/api/commission")
    fun salesCommission(
        @Query("user_id") user_id: Int
    ): Single<Response<salesCommisssion>>

    @Headers("Connection:close")
    @GET("/api/customer/customerorder")
    fun customerOrder(
        @Query("employeeid") employeeid: Int
    ): Observable<Response<customerProductOrder>>

    @Headers("Connection:close")
    @GET("/api/customer/skuordered")
    fun skuTotalOrder(
        @Query("orderid") orderid: Int
    ): Observable<Response<skuOrderd>>

    @Headers("Connection:close")
    @GET("/api/customer/orderproducts")
    fun orderProduct(
        @Query("employeeid") employeeid: Int,
        @Query("orderid") orderid: Int
    ): Observable<Response<realOrder>>

    @Headers("Connection:close")
    @GET("/api/customer/sendtokens")
    fun sendTokenToday(
        @Query("unro") unro: Int
    ): Observable<Response<sendTokenToIndividualCustomer>>
}

