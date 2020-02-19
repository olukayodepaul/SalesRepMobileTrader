package com.mobbile.paul.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "spiners")
data class spinersEntity(
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var id: Int = 0,
    var name: String = "",
    var sep: Int = 0
)

@Entity(tableName = "modules")
data class modulesEntity(
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var id: Int = 0,
    var nav: Int = 0,
    var name: String = "",
    var imageurl: String = ""
)

@Parcelize
@Entity(tableName = "customers")
data class customersEntity(
    @PrimaryKey(autoGenerate = false)
    var auto: Int = 0,
    var rep_id: Int = 0,
    var urno: Int = 0,
    var customerno: String = "",
    var outletclassid: Int = 0,
    var outletlanguageid: Int = 0,
    var outlettypeid: Int = 0,
    var outletname: String = "",
    var outletaddress: String = "",
    var contactname: String = "",
    var contactphone: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var token: String = "",
    var defaulttoken: String = "",
    var sequenceno: Int = 0,
    var distance: String = "",
    var duration: String = "",
    var mode: String = "",
    var dates: String = "",
    var volumeclass: String = "",
    var customer_code: String = "",
    var sort: Int = 0,
    var notice: String = "",
    var entry_time: String = ""
): Parcelable

@Entity(tableName = "custometvisitsequence")
data class EntityCustomerVisitSequence(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nexts: Int = 0,
    val self:String=""
)

@Entity(tableName = "salesentries")
data class EntityGetSalesEntry (
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var product_id: String = "",
    var product_code: String = "",
    var product_name: String = "",
    var soq: String = "",
    var seperator: String = "",
    var seperatorname: String = "",
    var pricing: String = "",
    var inventory: String = "",
    var orders: String = "",
    var amount: Double = 0.0,
    var entry_time: String = "",
    var controlpricing: String = "",
    var controlinventory: String = "",
    var controlorder: String = "",
    var comision: Double = 0.0
)

@Entity(tableName = "chatmessage")
data class ChatMessage(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    val status: Int = 0,
    val message:String="",
    val timeago:String="",
    val dates:String=""
)




