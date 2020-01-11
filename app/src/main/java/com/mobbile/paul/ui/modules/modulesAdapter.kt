package com.mobbile.paul.ui.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobbile.paul.model.modulesEntity
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.modules_adapter.view.*


class modulesAdapter(private var mItems: List<modulesEntity>, private var contexts: Context,
                     val clickListener: (modulesEntity) -> Unit
                     ):
    RecyclerView.Adapter<modulesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.modules_adapter, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item,clickListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    companion object {
        private val TAG = "ModulesActivity"
    }

    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: modulesEntity, clickListener: (modulesEntity) -> Unit) {
            containerView.tv_name.text = item.name

            Glide.with(contexts)
                .load(item.imageurl)
                .override(50,50)
                .into(containerView.imageView)

            containerView.setOnClickListener {
                clickListener(item)
            }
        }
    }
}