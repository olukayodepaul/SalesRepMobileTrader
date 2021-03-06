package com.mobbile.paul.util

import androidx.lifecycle.MutableLiveData
import com.mobbile.paul.model.*

object ParserModel {

    fun LoginParser(
        data: MutableLiveData<LoginExchange>,
        sep: Int,
        status:Int,
        date: String,
        massage:String,
        employee_id:Int,
        name:String,
        notification:String,
        region_id:Int

    ) {
        val dataPasser = LoginExchange()
        dataPasser.sep = sep
        dataPasser.status = status
        dataPasser.date = date
        dataPasser.massage = massage
        dataPasser.employee_id = employee_id
        dataPasser.name = name
        dataPasser.notification = notification
        dataPasser.region_id = region_id
        data.postValue(dataPasser)
    }

    fun ExchangeParser(
        data: MutableLiveData<CustomerExchage>,
        allcustomers: List<customersEntity>,
        status: Int,
        msg:String

    ) {
        val dataPasser = CustomerExchage()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.allcustomers = allcustomers
        data.postValue(dataPasser)
    }

    fun outletSequenceParser(
        data: MutableLiveData<SequenceExchage>,
        status: Int,
        currentLat:Double,
        currentLng:Double

    ) {
        val dataPasser = SequenceExchage()
        dataPasser.status = status
        dataPasser.currentLat = currentLat
        dataPasser.currentLng = currentLng
        data.postValue(dataPasser)
    }

    fun salesEntriesParsers(
        data: MutableLiveData<SalesEntryExchage>,
        status: String,
        msg:String,
        allcustomers: List<setSalesEntry>

    ) {
        val dataPasser = SalesEntryExchage()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.data = allcustomers
        data.postValue(dataPasser)
    }

    fun repBaskets(
        data: MutableLiveData<ProductParser>,
        status: Int,
        msg: String,
        list: List<Products>

    ) {
        val dataPasser = ProductParser()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.list = list
        data.postValue(dataPasser)
    }

    fun AttendanExchange(
        data: MutableLiveData<AttendantParser>,
        status: Int,
        notis: String
    ){
        val dataPasser = AttendantParser()
        dataPasser.status = status
        dataPasser.notis = notis
        data.postValue(dataPasser)
    }

    fun mSalesDetails(
        data: MutableLiveData<mSalesDetailsParser>,
        status: Int,
        msg: String,
        list: List<AllSalesDetails>

    ) {
        val dataPasser = mSalesDetailsParser()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.list = list
        data.postValue(dataPasser)
    }

    fun mBankDetails(
        data: MutableLiveData<mBankDetailsParser>,
        status: Int,
        msg: String,
        list: BankDetails

    ) {
        val dataPasser = mBankDetailsParser()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.list = list
        data.postValue(dataPasser)
    }

    fun mDetailsForEachSales(
        data: MutableLiveData<mDetailsForEachSalesParser>,
        status: Int,
        msg: String,
        list: DetailsForEachSales

    ) {
        val dataPasser = mDetailsForEachSalesParser()
        dataPasser.status = status
        dataPasser.msg = msg
        dataPasser.list = list
        data.postValue(dataPasser)
    }






}


