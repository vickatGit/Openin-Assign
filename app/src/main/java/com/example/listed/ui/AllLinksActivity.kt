package com.example.listed.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listed.R
import com.example.listed.data.model.TopLinksItem
import com.example.listed.ui.adapter.LinksAdapter
import com.example.listed.ui.listener.CopyListener

class AllLinksActivity : AppCompatActivity() {
    private var allLinks:ArrayList<TopLinksItem?>?=null
    private lateinit var linkSearcher:SearchView
    private lateinit var linksRecycler:RecyclerView
    private lateinit var linksAdapter: LinksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_links)
        allLinks=intent.getParcelableArrayListExtra(MainActivity.LINKS_DATA_MSG)

        allLinks.let {links ->
            initialiseViews()
            linksRecycler.layoutManager=LinearLayoutManager(this@AllLinksActivity)
            linksAdapter=LinksAdapter(allLinks, object : CopyListener {
                override fun linkClick(link: String) {
                    saveToClipboard(this@AllLinksActivity,link)
                }
            })
            linksRecycler.adapter=linksAdapter

        }
        linkSearcher.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                linksAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun initialiseViews() {
        linkSearcher=findViewById(R.id.link_searcher)
        linksRecycler=findViewById(R.id.links_recycler)
    }
    fun saveToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(
            this@AllLinksActivity,
            "Smart Link copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }
}