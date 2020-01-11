package com.mobbile.paul.provider

import com.mobbile.paul.model.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject
constructor(private val appDao: AppDao, private val api: Api) {

    fun Login(
        username: String,
        password: String,
        imei: String
    ): Single<Response<ApplicationLogin>> =
        api.Login(username, password, imei)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it
            }

    fun deleteModules() = Observable.fromCallable {
        appDao.deleteModules()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteSpiners() = Observable.fromCallable {
        appDao.deleteSpiners()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteCustomers() = Observable.fromCallable {
        appDao.deleteCustomers()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun insertIntoSpinnerAndModulesTable(
        modules: List<modulesEntity>,
        spinners: List<spinersEntity>
    ) = Observable.fromCallable {
        appDao.insertIntoSpinnerAndModulesTable(
            modules, spinners
        )
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun fetchModules(): Single<List<modulesEntity>> =
        Single.fromCallable {
            appDao.fetchModules()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getCustomersEntryCount(): Single<Int> =
        Single.fromCallable {
            appDao.getCustomersEntryCount()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getAllCustomers(
        employee_id: Int
    ): Single<Response<CustomersRemoteResponse>> =
        api.getAllCustomers(employee_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun saveAllCustomerToLocalDB(
        alloutlets: List<customersEntity>
    ) = Observable.fromCallable {
        appDao.saveAllCustomerToLocalDB(
            alloutlets
        )
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun pullFromCustomerLocalDB(): Single<List<customersEntity>> =
        Single.fromCallable {
            appDao.pullFromCustomerLocalDB()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun validateOutletSequence(id: Int): Single<EntityCustomerVisitSequence> =
        Single.fromCallable{
            appDao.validateOutletSequence(id)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getBasket(customerno: String, customer_code: String, repid: Int): Single<Response<InitBasket>> =
        api.getBasket(customerno, customer_code, repid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun deleteEntityGetSalesEntry() = Observable.fromCallable {
        appDao.deleteEntityAndGetSalesEntry()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun createDailySales(
        salesen: List<EntityGetSalesEntry>
    ) =
        Observable.fromCallable { appDao.saveSalesEntry(salesen) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateDailySales(inventory: Double, pricing: Int, order: Double, entry_time: String, controlpricing:String, controlinventory:String, controlorder:String, product_code:String) =
        Single.fromCallable{
            appDao.updateDailySales(inventory,pricing,order,entry_time,controlpricing,controlinventory,controlorder,product_code)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun validateSalesEntry(): Observable<Int> =
        Observable.fromCallable {
            appDao.validateSalesEntry()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun fetchAllEntryPerDaily(): Observable<List<EntityGetSalesEntry>> =
        Observable.fromCallable {
            appDao.fetchAllEntryPerDay()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun SumAllTotalSalesEntry(): Observable<SumSales> =
        Observable.fromCallable {
            appDao.SumAllTotalSalesEntry()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getCustomerNo(customerno: String): Single<Response<Basket>> =
        api.getCustomerNo(customerno)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun takeAttendant(taskid: Int, repid: Int, outletlat:Double, outletlng:Double, currentLat:Double,
                      currentLng:Double, distance:String, duration:String, sequenceno:String, arrivaltime:String): Single<Response<Attendant>> =
        api.takeAttendant(taskid, repid, outletlat, outletlng, currentLat, currentLng, distance, duration, sequenceno, arrivaltime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun setAttendantTime(time: String) =
        Single.fromCallable{
            appDao.setAttendantTime(time)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun SequencetManager(id: Int, nexts:Int, self:String) =
        Single.fromCallable{
            appDao.SequencetManager(id, nexts, self)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun CloseOutlets(repid: Int,  currentlat: String, currentlng: String,
                     outletlat: String, outletlng: String, arrivaltime: String,
                     visitsequence: Int, distance: String, duration: String, urno: Int): Single<Response<Attendant>> =
        api.CloseOutlets(repid,currentlat,currentlng,outletlat, outletlng,arrivaltime, visitsequence,distance,duration,urno)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{it}

    fun setEntryTime(time: String, auto:Int) =
        Single.fromCallable{
            appDao.setEntryTime(time, auto)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun UpdateSeque(id: Int,nexts:Int,self:String) =
        Single.fromCallable{
            appDao.UpdateSeque(id,nexts,self)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun fetchPostSales(data: postToServer): Single<Response<Attendant>> =
        api.fetchPostSales(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {it}

    fun pullAllSalesEntry(): Single<List<EntityGetSalesEntry>> =
        Single.fromCallable {
            appDao.pullAllSalesEntry()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}