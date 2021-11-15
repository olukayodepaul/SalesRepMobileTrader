package com.mobbile.paul.ui.entryhistory.repository

import com.mobbile.paul.model.Attendant
import com.mobbile.paul.model.EntityGetSalesEntry
import com.mobbile.paul.model.postToServer


interface EntryRepository {
    suspend fun fetchAllEntryPerDailys(): List<EntityGetSalesEntry>
    suspend fun pullAllSalesEntry():List<EntityGetSalesEntry>
    suspend fun postSales(data: postToServer): Attendant
}