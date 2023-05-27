package com.example.listed.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.listed.R
import com.example.listed.data.model.TopLinksItem
import com.example.listed.ui.listener.CopyListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LinksAdapter(val linkList: ArrayList<TopLinksItem?>?,val  copyListener: CopyListener)
    : RecyclerView.Adapter<LinksAdapter.LinkHolder>(),Filterable {

    private var filteredList: ArrayList<TopLinksItem?>? = null
    init {
        filteredList = linkList?.toMutableList() as ArrayList<TopLinksItem?>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.link_item_, parent, false)
        return LinkHolder(view)
    }

    override fun onBindViewHolder(holder: LinkHolder, position: Int) {
        val linkItem = filteredList?.get(holder.adapterPosition)
        holder.siteName.text = linkItem?.app
        holder.totalLinkClicks.text = linkItem?.total_clicks.toString()
        holder.siteLink.text = linkItem?.web_link
        holder.linkCreationTime.text = formatToNormalDate(linkItem?.created_at.toString())
        holder.copyLink.setOnClickListener {
            linkItem?.web_link?.let { it1 -> copyListener.linkClick(it1) }
        }
        holder.siteThumb.load(linkItem?.original_image){
            placeholder(R.drawable.link_icon)
        }
    }

    override fun getItemCount(): Int {
        return filteredList?.size!!
    }

    class LinkHolder(itemView: View) : ViewHolder(itemView) {
        val siteThumb: ImageView = itemView.findViewById(R.id.link_thumb)
        val siteName: TextView = itemView.findViewById(R.id.website_name);
        val linkCreationTime: TextView = itemView.findViewById(R.id.timestamp)
        val totalLinkClicks: TextView = itemView.findViewById(R.id.total_clicks);
        val siteLink: TextView = itemView.findViewById(R.id.url)
        val copyLink: ImageView = itemView.findViewById(R.id.copy);
    }

    fun formatToNormalDate(inputDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val calendar = Calendar.getInstance()
            calendar.time = inputFormat.parse(inputDate)

            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
            outputFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = ArrayList<TopLinksItem?>()
                val query = constraint?.toString()?.toLowerCase(Locale.getDefault())

                if (query.isNullOrEmpty()) {
                    filteredResults.addAll(linkList as ArrayList<TopLinksItem?>)
                } else {
                    linkList?.forEach { link ->
                        link?.web_link?.toLowerCase(Locale.getDefault())?.let { webLink ->
                            if (webLink.contains(query)) {
                                filteredResults.add(link)
                            }
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? ArrayList<TopLinksItem?>
                notifyDataSetChanged()
            }
        }
    }
}