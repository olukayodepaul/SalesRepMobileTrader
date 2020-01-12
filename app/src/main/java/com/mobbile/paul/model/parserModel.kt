package com.mobbile.paul.model

data class LoginExchange(
    var sep: Int = 0,
    var status: Int = 0,
    var date: String = "",
    var massage: String= "",
    var employee_id: Int = 0,
    var name: String = "",
    var notification: String = ""
)

data class CustomerExchage(
    var status: Int = 0,
    var msg:String="",
    var allcustomers: List<customersEntity>? = null
)

data class SequenceExchage(
    var status: Int = 0,
    var msg:String="",
    var switchers:Int = 0,
    var currentLat:Double = 0.0,
    var currentLng:Double= 0.0
)

data class SalesEntryExchage (
    var status: String? = null,
    var msg: String? = null,
    var data: List<setSalesEntry>? = null
)

data class SumSales(
    var spricing: Int = 0,
    var sinventory: Double = 0.0,
    var sorder: Double = 0.0,
    var samount: Double = 0.0
)

data class ProductParser (
    var status: Int = 0,
    var msg: String = "",
    var list: List<Products>? = null
)

data class AttendantParser (
    var status: Int = 0,
    var notis: String = ""
)

data class mSalesDetailsParser (
    var status: Int = 0,
    var msg: String = "",
    var list: List<AllSalesDetails>? = null
)

data class mBankDetailsParser (
    var status: Int = 0,
    var msg: String = "",
    var list: BankDetails? = null
)

data class mDetailsForEachSalesParser (
    var status: Int = 0,
    var msg: String = "",
    var list: DetailsForEachSales? = null
)

