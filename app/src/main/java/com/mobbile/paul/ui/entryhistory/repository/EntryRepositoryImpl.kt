package com.mobbile.paul.ui.entryhistory.repository

import com.mobbile.paul.model.Attendant
import com.mobbile.paul.model.EntityGetSalesEntry
import com.mobbile.paul.model.postToServer
import com.mobbile.paul.provider.Api
import com.mobbile.paul.provider.AppDao
import javax.inject.Inject


class EntryRepositoryImpl @Inject constructor(
    private val appDao: AppDao, private val api: Api
) :
    EntryRepository {

    override suspend fun fetchAllEntryPerDailys(): List<EntityGetSalesEntry> {
        return appDao.fetchAllEntryPerDay()
    }

    override suspend fun pullAllSalesEntry(): List<EntityGetSalesEntry> {
        return appDao.pullAllSalesEntry()
    }

    override suspend fun postSales(data: postToServer): Attendant {
        return api.postSales(data)
    }

}