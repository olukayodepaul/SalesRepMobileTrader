package com.mobbile.paul.provider

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobbile.paul.model.*


@Database(entities = [modulesEntity::class, spinersEntity::class, customersEntity::class,
    EntityCustomerVisitSequence::class,EntityGetSalesEntry::class,ChatMessage::class],version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val appdao: AppDao

    companion object {
        val DATABASE_NAME = "rep_mobile_trader"
    }
}