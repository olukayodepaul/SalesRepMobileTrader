package com.mobbile.paul.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ApplicationLogin(
    @SerializedName("status")
    @Expose
    var status: Int,
    @SerializedName("massage")
    @Expose
    var massage: String = "",
    @SerializedName("notification")
    @Expose
    var notification: String = "",
    @SerializedName("employee_id")
    @Expose
    var employee_id: Int,
    @SerializedName("dates")
    @Expose
    var dates: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("region_id")
    @Expose
    var region_id: Int,
    @SerializedName("modules")
    @Expose
    var modules: List<ApplicationModules>? = null,
    @SerializedName("spinners")
    @Expose
    var spinners: List<ApplicationSpinners>? = null
)

data class ApplicationModules(
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("nav")
    @Expose
    var nav: Int = 0,
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("imageurl")
    @Expose
    var imageurl: String = ""
)

data class ApplicationSpinners(
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("sep")
    @Expose
    var sep: Int = 0
)

data class CustomersRemoteResponse(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = "",
    @SerializedName("alloutlets")
    @Expose
    var alloutlets: List<ApplicationCustomers> = emptyList()
)

@Parcelize
data class ApplicationCustomers(
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("rep_id")
    @Expose
    var rep_id: Int = 0,
    @SerializedName("urno")
    @Expose
    var urno: Int = 0,
    @SerializedName("customerno")
    @Expose
    var customerno: String = "",
    @SerializedName("outletclassid")
    @Expose
    var outletclassid: Int = 0,
    @SerializedName("outletlanguageid")
    @Expose
    var outletlanguageid: Int = 0,
    @SerializedName("outlettypeid")
    @Expose
    var outlettypeid: Int = 0,
    @SerializedName("outletname")
    @Expose
    var outletname: String = "",
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String = "",
    @SerializedName("contactname")
    @Expose
    var contactname: String = "",
    @SerializedName("contactphone")
    @Expose
    var contactphone: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: Double = 0.0,
    @SerializedName("longitude")
    @Expose
    var longitude: Double = 0.0,
    @SerializedName("token")
    @Expose
    var token: String = "",
    @SerializedName("defaulttoken")
    @Expose
    var defaulttoken: String = "",
    @SerializedName("sequenceno")
    @Expose
    var sequenceno: Int = 0,
    @SerializedName("distance")
    @Expose
    var distance: String = "",
    @SerializedName("duration")
    @Expose
    var duration: String = "",
    @SerializedName("mode")
    @Expose
    var mode: String = "",
    @SerializedName("dates")
    @Expose
    var dates: String = "",
    @SerializedName("volumeclass")
    @Expose
    var volumeclass: String = "",
    @SerializedName("customer_code")
    @Expose
    var customer_code: String = "",
    @SerializedName("sort")
    @Expose
    var sort: Int = 0,
    @SerializedName("notice")
    @Expose
    var notice: String = "",
    @SerializedName("entry_time")
    @Expose
    var entry_time: String = ""
): Parcelable

data class InitBasket (
    @SerializedName("status")
    @Expose
    var status: String = "",
    @SerializedName("sales")
    @Expose
    var sales: List<setSalesEntry>?=null,
    @SerializedName("entry")
    @Expose
    var entry: List<getSalesEntry>?=null
)

data class setSalesEntry (
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("product_id")
    @Expose
    var product_id: String = "",
    @SerializedName("product_code")
    @Expose
    var product_code: String = "",
    @SerializedName("product_name")
    @Expose
    var product_name: String = "",
    @SerializedName("soq")
    @Expose
    var soq: String = "",
    @SerializedName("seperator")
    @Expose
    var seperator: String = "",
    @SerializedName("order_sold")
    @Expose
    var order_sold: Double = 0.0
)

data class getSalesEntry (
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("product_id")
    @Expose
    var product_id: String = "",
    @SerializedName("product_code")
    @Expose
    var product_code: String = "",
    @SerializedName("product_name")
    @Expose
    var product_name: String = "",
    @SerializedName("soq")
    @Expose
    var soq: String = "",
    @SerializedName("seperator")
    @Expose
    var seperator: String = "",
    @SerializedName("seperatorname")
    @Expose
    var seperatorname: String = "",
    @SerializedName("pricing")
    @Expose
    var pricing: String = "",
    @SerializedName("inventory")
    @Expose
    var inventory: String = "",
    @SerializedName("orders")
    @Expose
    var orders: String = "",
    @SerializedName("amount")
    @Expose
    var amount: Double = 0.0,
    @SerializedName("entry_time")
    @Expose
    var entry_time: String = "",
    @SerializedName("controlpricing")
    @Expose
    var controlpricing: String = "",
    @SerializedName("controlinventory")
    @Expose
    var controlinventory: String = "",
    @SerializedName("controlorder")
    @Expose
    var controlorder: String = "",
    @SerializedName("comision")
    @Expose
    var comision: Double = 0.0

)

data class Products(
    @SerializedName("productname")
    @Expose
    var productname: String = "",
    @SerializedName("qty")
    @Expose
    var qty: String = ""
)

data class Basket (
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = "",
    @SerializedName("allrepsproducts")
    @Expose
    var allrepsproducts: List<Products>? = null
)

data class Attendant(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = ""
)

data class postToServer(
    @SerializedName("repid")
    @Expose
    var repid: Int = 0,
    @SerializedName("currentlat")
    @Expose
    var currentlat: String = "",
    @SerializedName("currentlng")
    @Expose
    var currentlng: String = "",
    @SerializedName("outletlat")
    @Expose
    var outletlat: String = "",
    @SerializedName("outletlng")
    @Expose
    var outletlng: String = "",
    @SerializedName("arrivaltime")
    @Expose
    var arrivaltime: String = "",
    @SerializedName("visitsequence")
    @Expose
    var visitsequence: Int = 0,
    @SerializedName("distance")
    @Expose
    var distance: String = "",
    @SerializedName("duration")
    @Expose
    var duration: String = "",
    @SerializedName("urno")
    @Expose
    var urno: Int = 0,
    @SerializedName("token")
    @Expose
    var token: String = "",
    @SerializedName("uiid")
    @Expose
    var uiid: String = "",
    @SerializedName("lists")
    @Expose
    var lists: List<getSalesEntry>? = null
)

data class SalesDetails(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = "",
    @SerializedName("details")
    @Expose
    var details: List<AllSalesDetails> = emptyList()
)

data class AllSalesDetails(
    @SerializedName("product_name")
    @Expose
    var product_name: String = "",
    @SerializedName("product_code")
    @Expose
    var product_code: String = "",
    @SerializedName("qty")
    @Expose
    var qty: Double = 0.0,
    @SerializedName("ordered")
    @Expose
    var ordered: Double = 0.0
)

data class  BankDetails(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = "",
    @SerializedName("sumdeposit")
    @Expose
    var sumdeposit: Double= 0.0,
    @SerializedName("sumorder")
    @Expose
    var sumorder: Double= 0.0,
    @SerializedName("sumcom")
    @Expose
    var sumcom: Double= 0.0,
    @SerializedName("details")
    @Expose
    var details: List<AllBankDetails> = emptyList()
)

data class AllBankDetails(
    @SerializedName("product_name")
    @Expose
    var product_name: String = "",
    @SerializedName("product_code")
    @Expose
    var product_code: String = "",
    @SerializedName("qty")
    @Expose
    var qty: Double = 0.0,
    @SerializedName("ordered")
    @Expose
    var ordered: Double = 0.0,
    @SerializedName("price")
    @Expose
    var price: Double = 0.0,
    @SerializedName("com")
    @Expose
    var com: Double = 0.0,
    @SerializedName("deposit")
    @Expose
    var deposit: Double = 0.0
)

data class  DetailsForEachSales(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = "",
    @SerializedName("details")
    @Expose
    var details: List<AllDetailsForEachSales> = emptyList()
)

data class AllDetailsForEachSales(
    @SerializedName("product_name")
    @Expose
    var product_name: String = "",
    @SerializedName("product_code")
    @Expose
    var product_code: String = "",
    @SerializedName("qty")
    @Expose
    var qty: Double = 0.0,
    @SerializedName("pricing")
    @Expose
    var pricing: Double = 0.0,
    @SerializedName("inventory")
    @Expose
    var inventory: Double = 0.0,
    @SerializedName("product_category")
    @Expose
    var product_category: String = ""
)

data class OutletUpdateResponse(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = ""
)

data class OutletAsyn(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("outletclassid")
    @Expose
    var outletclassid: Int = 0,
    @SerializedName("outletlanguageid")
    @Expose
    var outletlanguageid: Int = 0,
    @SerializedName("outlettypeid")
    @Expose
    var outlettypeid: Int = 0,
    @SerializedName("outletname")
    @Expose
    var outletname: String = "",
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String = "",
    @SerializedName("contactname")
    @Expose
    var contactname: String = "",
    @SerializedName("contactphone")
    @Expose
    var contactphone: String = "",
    @SerializedName("longitude")
    @Expose
    var longitude: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: String = ""
)

data class salesCommisssion (
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("comlist")
    @Expose
    var comlist: List<salesCommissionList>? = null
)

data class salesCommissionList(
    @SerializedName("commisssiontype")
    @Expose
    var commisssiontype: String = "",
    @SerializedName("dates")
    @Expose
    var dates: String = "",
    @SerializedName("kays")
    @Expose
    var kays: String = "",
    @SerializedName("amount")
    @Expose
    var amount: String = ""
)

data class customerProductOrder(
    @SerializedName("order")
    @Expose
    var order: List<allCustomerProductOrder>?  = null
)

data class allCustomerProductOrder(
    @SerializedName("orderid")
    @Expose
    var orderid: Int = 0,
    @SerializedName("outletname")
    @Expose
    var outletname: String = "",
    @SerializedName("urno")
    @Expose
    var urno: Int = 0,
    @SerializedName("customerno")
    @Expose
    var customerno: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: String = "",
    @SerializedName("longitude")
    @Expose
    var longitude: String = "",
    @SerializedName("employeeid")
    @Expose
    var employeeid: Int = 0,
    @SerializedName("contactphone")
    @Expose
    var contactphone: String = "",
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String = "",
    @SerializedName("uid")
    @Expose
    var uid: String = "",
    @SerializedName("token")
    @Expose
    var token: Int = 0
)


data class skuOrderd(
    @SerializedName("skuorder")
    @Expose
    var skuorder: List<allskuOrderd>?  = null,
    @SerializedName("totalqty")
    @Expose
    var totalqty: Double = 0.0,
    @SerializedName("totalamount")
    @Expose
    var totalamount: Double = 0.0
)

data class allskuOrderd(
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("customerinfo")
    @Expose
    var customerinfo: Int = 0,
    @SerializedName("qtyordered")
    @Expose
    var qtyordered: Int = 0,
    @SerializedName("skuname")
    @Expose
    var skuname: String = "",
    @SerializedName("productcode")
    @Expose
    var productcode: String = "",
    @SerializedName("amount")
    @Expose
    var amount: Double = 0.0
)


