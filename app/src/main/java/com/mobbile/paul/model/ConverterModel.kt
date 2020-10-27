package com.mobbile.paul.model

fun ApplicationModules.toModules(): modulesEntity {
    return modulesEntity(
        auto,id,nav,name,imageurl
    )
}

fun ApplicationSpinners.toSpinner(): spinersEntity {
    return spinersEntity(
        auto,id,name,sep
    )
}

fun ApplicationCustomers.toEntityAllOutletsList(): customersEntity {
    return customersEntity(
        auto,rep_id,urno,customerno,outletclassid,outletlanguageid,outlettypeid,outletname,outletaddress,
        contactname,contactphone,latitude,longitude,token,defaulttoken,sequenceno,distance,duration,mode,
        dates,volumeclass,customer_code,sort,notice,entry_time, depotwaivers
    )
}

fun getSalesEntry.toEntityGetSalesEntry(): EntityGetSalesEntry {
    return EntityGetSalesEntry(
        id,product_id, product_code,product_name, soq, seperator,seperatorname, pricing, inventory,orders,amount,
        entry_time,controlpricing,controlinventory,controlorder,comision
    )
}

fun EntityGetSalesEntry.toAllOutletsList(): getSalesEntry {
    return getSalesEntry(
        id,product_id,product_code,product_name,soq,seperator,seperatorname,pricing,inventory,orders,amount,
        entry_time,controlpricing,controlinventory,controlorder,comision
    )
}