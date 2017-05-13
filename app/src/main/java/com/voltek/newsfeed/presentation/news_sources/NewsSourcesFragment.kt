package com.voltek.newsfeed.presentation.news_sources

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.voltek.newsfeed.R
import com.voltek.newsfeed.data.entity.Source
import com.voltek.newsfeed.presentation.BaseFragment
import com.voltek.newsfeed.presentation.Event
import com.voltek.newsfeed.presentation.news_sources.NewsSourcesContract.NewsSourcesView
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar.*

class NewsSourcesFragment : BaseFragment(),
        NewsSourcesView {

    companion object {
        const val TAG = "NewsSourcesFragment"

        private val CATEGORY_ITEMS_IDS = arrayOf(
                R.id.action_all,
                R.id.action_enabled,
                R.id.action_business,
                R.id.action_entertainment,
                R.id.action_gaming,
                R.id.action_general,
                R.id.action_music,
                R.id.action_politics,
                R.id.action_science_and_nature,
                R.id.action_sport,
                R.id.action_technology
        )

        fun newInstance(): NewsSourcesFragment = NewsSourcesFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: NewsSourcesPresenter

    private lateinit var mAdapter: NewsSourcesAdapter

    private var mCategory: Int = R.id.action_all

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mAdapter = NewsSourcesAdapter(context, ArrayList<Source>())
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recycler_view.hasFixedSize()
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = ScaleInAnimationAdapter(mAdapter)
        recycler_view.itemAnimator = ScaleInLeftAnimator()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_news_sources, menu)
        menu?.findItem(mCategory)?.isChecked = true
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(mCategory)?.isChecked = true
    }

    override fun attachInputListeners() {
        RxToolbar.itemClicks(activity.toolbar)
                .filter { !swipe_container.isRefreshing }
                .subscribe({
                    when (it.itemId) {
                        in CATEGORY_ITEMS_IDS -> {
                            if (!it.isChecked) {
                                mPresenter.notify(
                                        Event.FilterSources(it.title.toString(), it.itemId)
                                )
                            }
                        }
                        R.id.action_refresh -> mPresenter.notify(Event.Refresh())
                    }
                })
                .bind()

        mAdapter.getOnItemClickObservable()
                .subscribe({ mPresenter.notify(Event.EnableNewsSource(it)) })
                .bind()
    }

    override fun detachInputListeners() {
        resetCompositeDisposable()
    }

    override fun render(model: NewsSourcesContract.NewsSourcesModel) {
        swipe_container.isEnabled = model.loading
        swipe_container.isRefreshing = model.loading

        mAdapter.replace(model.sources)

        mCategory = model.categoryId
        activity.invalidateOptionsMenu()

        when (model.categoryId) {
            R.id.action_all -> activity.title = getString(R.string.category_all)
            R.id.action_enabled -> activity.title = getString(R.string.category_enabled)
            R.id.action_business -> activity.title = getString(R.string.category_business)
            R.id.action_entertainment -> activity.title = getString(R.string.category_entertainment)
            R.id.action_gaming -> activity.title = getString(R.string.category_gaming)
            R.id.action_general -> activity.title = getString(R.string.category_general)
            R.id.action_music -> activity.title = getString(R.string.category_music)
            R.id.action_politics -> activity.title = getString(R.string.category_politics)
            R.id.action_science_and_nature -> activity.title = getString(R.string.category_science_and_nature)
            R.id.action_sport -> activity.title = getString(R.string.category_sport)
            R.id.action_technology -> activity.title = getString(R.string.category_technology)
        }

        if (!model.message.isEmpty()) {
            if (model.sources.isEmpty()) {
                tv_message.text = model.message
                tv_message.visibility = View.VISIBLE
            } else {
                tv_message.visibility = View.GONE
                tv_message.text = ""
                toast(model.message)
            }
        } else {
            tv_message.visibility = View.GONE
            tv_message.text = ""
        }
    }
}