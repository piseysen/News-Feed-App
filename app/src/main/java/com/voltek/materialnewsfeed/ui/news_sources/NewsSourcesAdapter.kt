package com.voltek.materialnewsfeed.ui.news_sources

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.voltek.materialnewsfeed.R
import com.voltek.materialnewsfeed.data.api.Source
import kotlinx.android.synthetic.main.item_source.view.*

class NewsSourcesAdapter(private val mContext: Context, private var mItems: MutableList<Source>)
    : RecyclerView.Adapter<NewsSourcesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logo = view.im_logo!!
        val name = view.tv_name!!
        val description = view.tv_description!!
        val category = view.tv_category!!
        val country = view.tv_country!!
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val view = layoutInflater.inflate(R.layout.item_article, parent, false)
        return NewsSourcesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = mItems[position]

        Glide.with(mContext).load(item.urlsToLogos[0]).into(holder?.logo)
        holder?.name?.text = item.name
        holder?.description?.text = item.description
        holder?.category?.text = item.category
        holder?.country?.text = item.country
    }

    override fun getItemCount(): Int = mItems.size
}
