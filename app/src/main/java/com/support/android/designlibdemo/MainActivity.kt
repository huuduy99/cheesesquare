/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import java.util.*

/**
 * https://github.com/codepath/android_guides/wiki/Handling-Scrolls-with-CoordinatorLayout
 * TODO
 */
class MainActivity : AppCompatActivity() {

    private var mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val ab = supportActionBar
        ab!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        ab.setDisplayHomeAsUpEnabled(true)

        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }

        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        if (viewPager != null) {
            setupViewPager(viewPager)
        }

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sample_actions, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> menu.findItem(R.id.menu_night_mode_system).isChecked = true
            AppCompatDelegate.MODE_NIGHT_AUTO -> menu.findItem(R.id.menu_night_mode_auto).isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> menu.findItem(R.id.menu_night_mode_night).isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> menu.findItem(R.id.menu_night_mode_day).isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.menu_night_mode_system -> setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            R.id.menu_night_mode_day -> setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.menu_night_mode_night -> setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.menu_night_mode_auto -> setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
        AppCompatDelegate.setDefaultNightMode(nightMode)

        if (Build.VERSION.SDK_INT >= 11) {
            recreate()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)
        adapter.addFragment(CheeseListFragment(), "Category 1")
        adapter.addFragment(CheeseListFragment(), "Category 2")
        adapter.addFragment(CheeseListFragment(), "Category 3")
        viewPager.adapter = adapter
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            true
        }
    }

    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitles[position]
        }
    }
}
