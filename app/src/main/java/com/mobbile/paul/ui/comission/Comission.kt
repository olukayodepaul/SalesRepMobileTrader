package com.mobbile.paul.ui.comission


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.model.salesCommissionList
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showProgressBar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_comission.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class Comission : DaggerFragment() {

    private lateinit var mAdapter: SalesCommissionAdapter

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: CommissionViewModel

    private var preferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comission, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmodel = ViewModelProviders.of(this, modelFactory)[CommissionViewModel::class.java]
        preferences = activity!!.getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        initViews()
        vmodel.salesCommission(preferences!!.getInt("preferencesEmployeeID",0)).observe(this, observers)
    }

    fun initViews() {
        _sales_recy_view.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        _sales_recy_view.layoutManager = layoutManager
    }

    private val observers = Observer<List<salesCommissionList>> {
        if (it != null) {
            showProgressBar(false, base_progress_bar)
            val list: List<salesCommissionList> = it
            mAdapter = SalesCommissionAdapter(list)
            mAdapter.notifyDataSetChanged()
            _sales_recy_view.adapter = mAdapter
        }
    }

}
