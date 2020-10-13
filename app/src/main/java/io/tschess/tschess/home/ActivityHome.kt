package io.tschess.tschess.home


import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.config.ActivityConfig
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.leaderboard.ActivityLeaderboard
import io.tschess.tschess.model.*
import io.tschess.tschess.profile.ActivityProfile
import io.tschess.tschess.server.CustomJsonArrayRequest
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class ActivityHome : AppCompatActivity(), Refresher, SwipeRefreshLayout.OnRefreshListener {

    private var size: Int = 9
    private var index: Int = 0
    private var fetched: Boolean = false
    private val parseGame: ParseGame = ParseGame()

    lateinit var progressBar: ProgressBar
    private lateinit var playerSelf: EntityPlayer
    private lateinit var adapterMenu: AdapterHome
    private lateinit var listMenu: ArrayList<EntityGame>

    override fun onRestart() {
        super.onRestart()
        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        this.onRefresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /* * */
        (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
        /* * */

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        extras.clear()

        this.listMenu = arrayListOf()
        this.adapterMenu = AdapterHome(this.playerSelf, applicationContext, this.listMenu, this)
        this.adapterMenu.refresher = this
        val listView: ListView = findViewById(R.id.list_view)
        listView.adapter = adapterMenu
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && listView.lastVisiblePosition - listView.headerViewsCount -
                    listView.footerViewsCount >= adapterMenu.count - 1
                ) {
                    fetchGames() // Now your listview has hit the bottom
                }
            }

            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
            }
        })
        this.swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this)
        this.fetchGames()

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)
        headerSelf.setListenerProfile(this.playerSelf)

        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                extras.putExtra("player_self", playerSelf)
                when (tab.position) {
                    0 -> {
                        //val tabAt: TabLayout.Tab = tabLayout.getTabAt(1)!!
                        //tabAt.icon = applicationContext.getDrawable(R.drawable.tab_menu)
                        //val intent = Intent(applicationContext, ActivityMenu::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        //applicationContext.startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(applicationContext, ActivityConfig::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        applicationContext.startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(applicationContext, ActivityLeaderboard::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        applicationContext.startActivity(intent)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })


    }

    private fun fetchGames() {
        if (fetched) {
            return
        }
        if (!this.swipeRefreshLayout.isRefreshing) {
            this.progressBar.visibility = View.VISIBLE
        }
        val url = "${ServerAddress().IP}:8080/game/menu"
        val params = HashMap<String, Any>()
        params["id"] = playerSelf.id
        params["index"] = this.index
        params["size"] = this.size
        params["self"] = true

        val jsonObject = JSONObject(params as Map<*, *>)
        val request =
            CustomJsonArrayRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    for (i: Int in 0 until response.length()) {
                        val game: EntityGame = parseGame.execute(response.getJSONObject(i))
                        listMenu.add(game)
                    }
                    this.adapterMenu.notifyDataSetChanged()
                    this.progressBar.visibility = View.INVISIBLE
                    this.swipeRefreshLayout.isRefreshing = false
                },
                {
                    fetched = true
                    this.progressBar.visibility = View.INVISIBLE
                    this.swipeRefreshLayout.isRefreshing = false
                }
            )
        VolleySingleton.getInstance(this).addToRequestQueue(request)
        this.index += 1
    }

    override fun onBackPressed() {
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)
        val intent = Intent(applicationContext, ActivityProfile::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        applicationContext.startActivity(intent)
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onRefresh() {
        /* * */
        (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
        /* * */
        this.progressBar.visibility = View.INVISIBLE
        this.adapterMenu.clear()
        this.index = 0
        this.fetched = false
        this.fetchGames()
    }

    override fun refresh(position: Int) {
        this.adapterMenu.clear()
        this.index = 0
        this.fetchGames()
    }

    fun dialogRematch(playerSelf: EntityPlayer, playerOther: EntityPlayer, game: EntityGame, action: String = "INVITATION") {
        val dialogRematch: DialogChallenge = DialogChallenge(this, playerSelf, playerOther, game, action)
        dialogRematch.show()
    }
}

@FunctionalInterface
interface Refresher {
    fun refresh(position: Int)
}