package com.mobbile.paul.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.ApplicationLogin
import com.mobbile.paul.model.LoginExchange
import com.mobbile.paul.model.toModules
import com.mobbile.paul.model.toSpinner
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ParserModel.LoginParser
import com.mobbile.paul.util.Util.getDate
import javax.inject.Inject

class LoginViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

    //abstraction
    private lateinit var data: ApplicationLogin

    private var response = MutableLiveData<LoginExchange>()

    fun LoginObserver(): MutableLiveData<LoginExchange> {
        return response
    }

    fun Login(username: String, password: String, imei: String, date: String) {

        repo.Login(username, password, imei)
            .subscribe({

                data = it.body()!!

                if (it.isSuccessful && it.body() != null && it.code() == 200 && it.body()!!.status == 200) {
                    if (data.modules!!.isEmpty() && data.spinners!!.isEmpty()) {
                        Log.d(TAG,"HERE 1")
                        LoginParser(
                            response,
                            0,
                            400,
                            data.dates,
                            data.massage,
                            data.employee_id,
                            data.name,
                            data.notification,
                            data.region_id
                        )
                    } else {

                        if (date != getDate()) {
                            Log.d(TAG,"HERE 2")
                            deleteModules()
                        } else {
                            Log.d(TAG,"HERE 3")
                            LoginParser(
                                response,
                                2,
                                data.status,
                                data.dates,
                                data.massage,
                                data.employee_id,
                                data.name,
                                data.notification,
                                data.region_id
                            )
                        }
                    }
                } else {
                    Log.d(TAG,"HERE 4")
                    LoginParser(
                        response,
                        0,
                        data.status,
                        "",
                        data.massage,
                        0,
                        "",
                        data.notification,
                        data.region_id
                    )
                }
            }, {
                LoginParser(response, 0, 400, data.dates, "FAIL", 0, "", "${it.message}",0)
            }).isDisposed
    }

    private fun deleteModules() {
        repo.deleteModules()
            .subscribe(
                {
                    deleteSpiners()
                }, {
                    LoginParser(response, 0, 400, data.dates, "FAIL", 0, "", "${it.message}",0)
                }
            ).isDisposed
    }

    private fun deleteSpiners() {
        repo.deleteSpiners()
            .subscribe(
                {
                    deleteCustomers()
                }, {
                    LoginParser(response, 0, 400, data.dates, "FAIL", 0, "", "${it.message}",0)
                }
            ).isDisposed
    }

    private fun deleteCustomers() {
        repo.deleteCustomers()
            .subscribe(
                {
                    insertIntoSpinnerAndModulesTable()
                }, {
                    LoginParser(response, 0, 400, data.dates, "FAIL", 0, "", "${it.message}",0)
                }
            ).isDisposed
    }

    private fun insertIntoSpinnerAndModulesTable() {
        repo.insertIntoSpinnerAndModulesTable(
            data.modules!!.map{it.toModules()},
            data.spinners!!.map{it.toSpinner()}
        ).subscribe({
            Log.d(TAG,"HERE 5")
            LoginParser(
                response,
                1,
                data.status,
                data.dates,
                data.massage,
                data.employee_id,
                data.name,
                data.notification,
                data.region_id
            )
        },{
            LoginParser(response, 0, 400, data.dates, "FAIL", 0, "", "${it.message}",0)
        }).isDisposed
    }

    companion object{
        val TAG = "LoginViewModel"
    }

}
