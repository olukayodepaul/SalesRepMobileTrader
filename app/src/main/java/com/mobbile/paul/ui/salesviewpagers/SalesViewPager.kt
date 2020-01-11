package com.mobbile.paul.ui.salesviewpagers


import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.comission.Comission
import com.mobbile.paul.ui.customers.Customers
import com.mobbile.paul.ui.totaldetails.DetailsFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sales_view_pager.*

class SalesViewPager : DaggerAppCompatActivity() {

    private val bt = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.sales -> {
                val fragment = Customers()
                titles.text = "Sales"
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_commission -> {
                val fragment = Comission()
                titles.text = "Commission"
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_details -> {
                val fragment = DetailsFragment()
                titles.text = "Details"
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.view_pager, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_view_pager)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        settings_btn.setOnClickListener {
            //here for asyc customers
            //get the count and show and hide the refresh buttons
        }

        val fragment = Customers()
        titles.text = "Sales"
        replaceFragment(fragment)
        navigations.setOnNavigationItemSelectedListener(bt)
    }
}
