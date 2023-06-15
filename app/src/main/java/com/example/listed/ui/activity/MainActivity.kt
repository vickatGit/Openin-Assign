package com.example.listed.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listed.R
import com.example.listed.data.local.AuthManager
import com.example.listed.data.model.TopLinksItem
import com.example.listed.databinding.ActivityAllLinksBinding
import com.example.listed.databinding.ActivityMainBinding
import com.example.listed.ui.fragment.CampaignFragment
import com.example.listed.ui.fragment.CourseFragment
import com.example.listed.ui.fragment.LinksFragment
import com.example.listed.ui.fragment.ProfileFragment
import com.example.listed.ui.listener.AllLinkListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding?=null
    private val binding get()=_binding!!

    private val BACK_PRESS_COUNT_THRESHOLD = 2
    private var backPressCount = 0
    private var exitToast: Toast? = null
    @Inject
    lateinit var authManager: AuthManager


    companion object{
        val  LINKS_DATA_MSG:String="all_links"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(authManager.getToken().isNullOrBlank()){
            authManager.setToken(resources.getString(R.string.auth_token))
        }
        binding.homeBottomNav.setOnItemSelectedListener {
            return@setOnItemSelectedListener when(it.itemId){
                R.id.link -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,LinksFragment()).commit()
                    binding.pageTitle.text = getString(R.string.dashboard_page_title)
                    true
                }
                R.id.courses -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,CourseFragment()).commit()
                    binding.pageTitle.text = getString(R.string.courses_page_title)
                    true
                }
                R.id.campaign -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,CampaignFragment()).commit()
                    binding.pageTitle.text = getString(R.string.campaign_page_title)
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container,ProfileFragment()).commit()
                    binding.pageTitle.text = getString(R.string.profile_page_title)
                    true
                }
                else -> {
                    false
                }
            }

        }
        binding.homeBottomNav.selectedItemId=R.id.link

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



}

