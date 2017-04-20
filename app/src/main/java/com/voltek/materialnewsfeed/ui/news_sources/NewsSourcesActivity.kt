package com.voltek.materialnewsfeed.ui.news_sources

import android.os.Bundle
import android.view.MenuItem
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.voltek.materialnewsfeed.R
import com.voltek.materialnewsfeed.ui.BaseActivity
import com.voltek.mvpdemo.library.navigation.proxy.NavigatorCommand
import kotlinx.android.synthetic.main.toolbar.*

class NewsSourcesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic)
        setupToolbar(displayHomeAsUpEnabled = true)

        if (supportFragmentManager.findFragmentByTag(NewsSourcesFragment.TAG) == null) {
            replaceFragment(
                    NewsSourcesFragment.newInstance(),
                    R.id.fragment_container,
                    NewsSourcesFragment.TAG)
        }
    }

    override fun executeCommand(commandId: NavigatorCommand): Boolean {
        return false
    }
}
