package com.mobbile.paul.ui.message

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.ChatMessage
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.synthetic.main.activity_users_list.*
import javax.inject.Inject


class UsersList : BaseActivity() {

    private lateinit var adapter: UserMessageAdapter

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)
        vmodel = ViewModelProviders.of(this, modelFactory)[MessageViewModel::class.java]
        initView()
        vmodel.MarkAsUnReadMessage()
    }

    private fun initView() {

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        message_recycler!!.setHasFixedSize(true)
        message_recycler.layoutManager = layoutManager
        listenToLocalDbMessage()
    }

    private fun listenToLocalDbMessage() {
        vmodel.getMessage().observe(this, ObserversModulesResult)
    }

    val ObserversModulesResult = Observer<List<ChatMessage>> {
        if (it.isNotEmpty()) {
            showProgressBar(false)
            val list: List<ChatMessage> = it
            adapter = UserMessageAdapter(list)
            adapter.notifyDataSetChanged()
            message_recycler.adapter = adapter
        }
    }
}
