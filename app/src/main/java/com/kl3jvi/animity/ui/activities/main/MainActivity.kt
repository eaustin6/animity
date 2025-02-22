package com.kl3jvi.animity.ui.activities.main

import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ActivityMainBinding
import com.kl3jvi.animity.utils.Constants.Companion.AUTHENTICATED_LOGIN_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.GUEST_LOGIN_TYPE
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var navController: NavController
    var isGuestLogin: Boolean = true
    var isConnected: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        with(intent) {
            when (getStringExtra("loginType")) {
                GUEST_LOGIN_TYPE -> isGuestLogin = true
                AUTHENTICATED_LOGIN_TYPE -> isGuestLogin = false
            }
        }
        if (intent.extras == null) {
            isGuestLogin = false
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAnalytics = Firebase.analytics

        turnOnStrictMode()

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorites,
                R.id.navigation_explore,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun hideBottomNavBar() {
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 500
        binding.navView.hide()
    }

    fun showBottomNavBar() {
        binding.navView.show()
        binding.navView.animate().translationY(0f).duration = 500
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            findNavController(
                this,
                R.id.nav_host_fragment_activity_main
            ).navigate(R.id.settingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun turnOnStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder().detectAll()
                .penaltyLog().penaltyFlashScreen().build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog().build()
        )
    }

    private fun handleNetworkChanges() {
        isConnectedToInternet().observe(this) { isConnected = it }
    }
}
