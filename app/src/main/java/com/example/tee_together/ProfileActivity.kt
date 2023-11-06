package com.example.tee_together

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize UI elements
        val profileUsername = findViewById<TextView>(R.id.profileUsername)
        drawerLayout = findViewById(R.id.drawerLayout)
        //val navView = findViewById<NavigationView>(R.id.navView)

        // Set the username
        profileUsername.text = "username"

        //popout menu
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //popout menu for signing out etc.
        /*navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_sign_out -> {
                    //action for signing out
                    finish()
                }
            }
            true
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
