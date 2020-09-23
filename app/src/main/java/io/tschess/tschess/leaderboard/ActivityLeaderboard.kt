package io.tschess.tschess.leaderboard

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.home.AdapterHome
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.server.CustomJsonArrayRequest
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class ActivityLeaderboard: AppCompatActivity(), Dialogger, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onRefresh() {
        this.progressBar.visibility = View.INVISIBLE
        this.adapterHome.clear()
        this.index = 0
        this.fetched = false
        this.fetchPlayers()
    }

    override fun onRestart() {
        super.onRestart()
        this.setTimerNote()
    }

    private lateinit var tabLayout: TabLayout

    private val pollingNote: Timer = Timer()
    private val parsePlayer: ParsePlayer = ParsePlayer()

    lateinit var playerSelf: EntityPlayer

    private var size: Int = 9
    private var index: Int = 0
    private lateinit var adapterHome: AdapterLeaderboard
    private lateinit var listHome: ArrayList<EntityPlayer>
    private lateinit var listView: ListView

    lateinit var progressBar: ProgressBar
    private var fetched: Boolean = false

    override fun onResume() {
        super.onResume()
        /* * */
        (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
        /* * */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /* * */
        (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
        /* * */

        this.progressBar = findViewById(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        extras.clear()

        this.listHome = arrayListOf()
        this.adapterHome = AdapterLeaderboard(this.playerSelf, this, this.listHome, this.progressBar, this)
        this.adapterHome.progressBar = this.progressBar
        this.adapterHome.dialogger = this
        this.listView = findViewById(R.id.list_view)
        this.listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && listView.lastVisiblePosition - listView.headerViewsCount -
                    listView.footerViewsCount >= listHome.size - 3
                ) {
                    fetchPlayers() // now your listView has hit the bottom
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
        this.listView.adapter = adapterHome
        this.swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this)
        this.fetchPlayers()

        this.tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onTabSelected(tab: TabLayout.Tab) {
                extras.putExtra("player_self", playerSelf)
                when (tab.position) {
                    1 -> {
                        //val tabAt: TabLayout.Tab = tabLayout.getTabAt(1)!!
                        //tabAt.icon = applicationContext.getDrawable(R.drawable.tab_menu)
                        //val intent = Intent(applicationContext, ActivityMenu::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        //applicationContext.startActivity(intent)
                    }
                    2 -> {
                        //val intent = Intent(applicationContext, ActivityConfig::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        //applicationContext.startActivity(intent)
                    }
                    0 -> {
                        //extras.putExtra("player_self", playerSelf)
                        //val intent = Intent(applicationContext, ActivityProfile::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        //applicationContext.startActivity(intent)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)

        this.setTimerNote()
    }

    private fun setTimerNote() {
        this.pollingNote.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getNote()
            }
        }, 3000, TimeUnit.SECONDS.toMillis(1))
    }

    private fun getNote() {
        val url = "${ServerAddress().IP}:8080/player/notify/${playerSelf.id}"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response: JSONObject ->
                if (response.has("fail")) {
                    return@Listener
                }
                val tabAt: TabLayout.Tab = this.tabLayout.getTabAt(1)!!
                tabAt.icon = applicationContext.getDrawable(R.drawable.note)
            }, Response.ErrorListener {
                Log.e("error in volley request", "${it.message}")
            })
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    override fun render(username: String) {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle("tschess")
        dialogBuilder.setMessage("${username} doesn't have any recent games")
            .setNeutralButton("ok") { dialog, _ ->
                dialog.cancel()
            }
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
    }

    private fun fetchPlayers() {
        if (fetched) {
            return
        }
        if (!this.swipeRefreshLayout.isRefreshing) {
            this.progressBar.visibility = View.VISIBLE
        }
        val url = "${ServerAddress().IP}:8080/player/leaderboard"
        val params = HashMap<String, String>()
        params["index"] = this.index.toString()
        params["size"] = this.size.toString()
        val jsonObject = JSONObject(params as Map<*, *>)
        val request = CustomJsonArrayRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                for (i: Int in 0 until response.length()) {
                    val json: JSONObject = response.getJSONObject(i)
                    val player: EntityPlayer = parsePlayer.execute(json)
                    this.listHome.add(player)
                }
                this.listHome.sortedWith(compareBy { it.rank })
                this.adapterHome.notifyDataSetChanged()
                this.progressBar.visibility = View.INVISIBLE
                this.swipeRefreshLayout.isRefreshing = false
            },
            {
                fetched = true
                this.progressBar.visibility = View.INVISIBLE
                this.swipeRefreshLayout.isRefreshing = false
            }
        )
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(this).addToRequestQueue(request)
        this.index += 1
    }

    override fun onBackPressed() {
        //val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        //extras.putExtra("player_self", playerSelf)
        //val intent = Intent(applicationContext, ActivityProfile::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        //applicationContext.startActivity(intent)
    }
}

@FunctionalInterface
interface Dialogger {
    fun render(username: String)
}


