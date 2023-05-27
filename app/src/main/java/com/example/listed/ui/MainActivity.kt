package com.example.listed.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listed.R
import com.example.listed.data.local.AuthManager
import com.example.listed.data.model.TopLinksItem
import com.example.listed.ui.fragment.CampaignFragment
import com.example.listed.ui.fragment.CourseFragment
import com.example.listed.ui.fragment.LinksFragment
import com.example.listed.ui.fragment.ProfileFragment
import com.example.listed.ui.listener.AllLinkListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),AllLinkListener {

    private val BACK_PRESS_COUNT_THRESHOLD = 2
    private var backPressCount = 0
    private var exitToast: Toast? = null
    @Inject
    lateinit var authManager: AuthManager

    private lateinit var homeBottomNav:BottomNavigationView
    private lateinit var pageTitle:TextView

    companion object{
        val  LINKS_DATA_MSG:String="all_links"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(authManager.getToken().isNullOrBlank()){
            authManager.setToken(resources.getString(R.string.auth_token))
        }

        initialiseViews()
        homeBottomNav.setOnItemSelectedListener {
            return@setOnItemSelectedListener when(it.itemId){
                R.id.link -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,LinksFragment()).commit()
                    pageTitle.text = getString(R.string.dashboard_page_title)
                    true
                }
                R.id.courses -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,CourseFragment()).commit()
                    pageTitle.text = getString(R.string.courses_page_title)
                    true
                }
                R.id.campaign -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,CampaignFragment()).commit()
                    pageTitle.text = getString(R.string.campaign_page_title)
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,ProfileFragment()).commit()
                    pageTitle.text = getString(R.string.profile_page_title)
                    true
                }
                else -> {
                    false
                }
            }

        }
        homeBottomNav.selectedItemId=R.id.link

    }

    private fun initialiseViews() {
        homeBottomNav=findViewById(R.id.home_bottom_nav)
        pageTitle=findViewById(R.id.page_title)
    }
    override fun onBackPressed() {
        if (backPressCount < BACK_PRESS_COUNT_THRESHOLD-1) {
            backPressCount++
            showToast("Press back again to exit")
        } else {
            exitToast?.cancel()
            super.onBackPressed()
        }
    }
    private fun showToast(message: String) {
        if (exitToast != null) {
            exitToast?.cancel()
        }
        exitToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        exitToast?.show()
    }

    override fun onClick(linkList: ArrayList<TopLinksItem?>?) {
        linkList?.let {
            val intent=Intent(this@MainActivity,AllLinksActivity::class.java)
            intent.putParcelableArrayListExtra(LINKS_DATA_MSG, it)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }


}

