package com.mobbile.paul.provider

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobbile.paul.model.*

@Dao
interface AppDao {

    @Query("DELETE FROM modules")
    fun deleteModules()

    @Query("DELETE FROM spiners")
    fun deleteSpiners()

    @Query("DELETE FROM customers")
    fun deleteCustomers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoSpinnerAndModulesTable(
        modules: List<modulesEntity>,
        spinners: List<spinersEntity>
    )

    @Query("SELECT * FROM modules")
    fun fetchModules(): List<modulesEntity>

    @Query("SELECT count(auto) from customers")
    fun getCustomersEntryCount():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllCustomerToLocalDB(
        alloutlets: List<customersEntity>
    )

    @Query("SELECT * FROM customers")
    fun pullFromCustomerLocalDB(): List<customersEntity>


    @Query("select * from custometvisitsequence where id=:id limit 1")
    fun validateOutletSequence(id: Int): EntityCustomerVisitSequence

    @Query("DELETE FROM salesentries")
    fun deleteEntityAndGetSalesEntry()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSalesEntry(
        salesen: List<EntityGetSalesEntry>
    )

    @Query("UPDATE salesentries SET inventory=:inventory, pricing=:pricing,orders=:order, entry_time=:entry_time, controlpricing=:controlpricing, controlinventory = :controlinventory, controlorder=:controlorder where  product_code=:product_code")
    fun updateDailySales(inventory: Double, pricing: Int, order: Double, entry_time: String, controlpricing:String, controlinventory:String, controlorder:String, product_code:String)

    @Query("SELECT count(id) FROM salesentries WHERE   controlpricing = '' OR controlinventory = '' OR controlorder = ''")
    fun validateSalesEntry() : Int

    @Query("SELECT * FROM salesentries order by seperator asc")
    fun fetchAllEntryPerDay(): List<EntityGetSalesEntry>

    @Query("SELECT  SUM(inventory) AS sinventory, SUM(pricing) AS spricing, SUM(orders) as sorder, SUM(amount) as samount FROM salesentries")
    fun SumAllTotalSalesEntry(): SumSales

    @Query("Update customers set entry_time =:time WHERE auto = 1")
    fun setAttendantTime(time: String)

    @Query("Insert into custometvisitsequence (id, nexts, self) values (:id, :nexts, :self)")
    fun SequencetManager(id: Int, nexts:Int, self:String)

    @Query("Update customers set entry_time =:time WHERE auto = :auto")
    fun setEntryTime(time: String, auto:Int)

    @Query("update custometvisitsequence set nexts=:nexts,self = (self || :self) where id = :id")
    fun UpdateSeque(id: Int,nexts:Int,self:String)

    @Query("SELECT * FROM salesentries")
    fun pullAllSalesEntry() : List<EntityGetSalesEntry>

    @Query("SELECT * FROM spiners")
    fun fetchSpinners() : List<spinersEntity>

    @Query("UPDATE customers SET outletclassid=:outletclassid, outletlanguageid=:outletlanguageid, outlettypeid=:outlettypeid, outletname=:outletname, outletaddress=:outletaddress, contactname=:contactname, contactphone=:contactphone, latitude=:latitude, longitude=:longitude where auto=:auto")
    fun updateIndividualCustomer(outletclassid:Int, outletlanguageid:Int, outlettypeid:Int, outletname:String, outletaddress:String, contactname:String, contactphone:String, latitude:Double, longitude:Double,auto:Int)

    @Query("Insert into chatmessage (uid, status, message, timeago, dates) values (:uid, :status, :message,:timeago,:date)")
    fun ChatMessage(uid: String, status:Int, message:String,timeago:String, date:String)

    @Query("SELECT * FROM chatmessage order by dates DESC")
    fun fetchMessages() : List<ChatMessage>

    @Query("SELECT count(uid) from chatmessage where status = 1")
    fun countUnReadMessage():Int

    @Query("Update chatmessage set status = 2")
    fun MarkAsUnReadMessage()

}