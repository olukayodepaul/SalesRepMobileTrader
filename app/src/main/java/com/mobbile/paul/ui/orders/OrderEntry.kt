package com.mobbile.paul.ui.orders


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobbile.paul.salesrepmobiletrader.R


class OrderEntry : AppCompatActivity() {

    private lateinit var nAdapter: OrdersEntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_entry)
    }
}
