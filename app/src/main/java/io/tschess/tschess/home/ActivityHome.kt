package io.tschess.tschess.home


import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.config.ActivityConfig
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.dialog.DialogOk
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.leaderboard.ActivityLeaderboard
import io.tschess.tschess.model.*
import io.tschess.tschess.profile.ActivityProfile
import io.tschess.tschess.server.CustomJsonArrayRequest
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import kotlinx.android.synthetic.main.card_home.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class ActivityHome : AppCompatActivity(), Refresher, SwipeRefreshLayout.OnRefreshListener {

    private var size: Int
    private var index: Int
    private var fetched: Boolean
    private val parseGame: ParseGame
    private val parsePlayer: ParsePlayer
    private var listMenu: ArrayList<EntityGame>

    private lateinit var rival0: CardHome
    private lateinit var rival1: CardHome
    private lateinit var rival2: CardHome
    private lateinit var glide: RequestManager
    private lateinit var playerSelf: EntityPlayer
    private lateinit var extendedDataHolder: ExtendedDataHolder

    init {
        this.size = 9
        this.index = 0
        this.fetched = false
        this.parseGame = ParseGame()
        this.parsePlayer = ParsePlayer()
        this.listMenu = arrayListOf()
    }

    lateinit var progressBar: ProgressBar
    lateinit var arrayAdapter: AdapterHome
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var notificationManager:  NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        this.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        this.notificationManager.cancelAll()

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        this.setTabListener(tabLayout)

        this.swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onResume() {
        super.onResume()
        this.notificationManager.cancelAll()
        this.glide = Glide.with(applicationContext)

        this.extendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extendedDataHolder.getExtra("player_self") as EntityPlayer
        this.extendedDataHolder.clear()

        /* * */
        this.getRivals()
        /* * */

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)
        headerSelf.setListenerProfile(this.playerSelf)

        val listView: ListView = findViewById(R.id.list_view)
        this.arrayAdapter = AdapterHome(this.playerSelf, applicationContext, this.listMenu, this)
        this.arrayAdapter.refresher = this
        listView.adapter = arrayAdapter
        this.setScrollListener(listView)

        this.index = 0
        this.fetched = false
        this.arrayAdapter.clear()
        this.fetchGames()
    }

    override fun onRefresh() {
        this.onResume()
    }

    override fun refresh() {
        this.onResume()
    }

    fun setShudder(rival: CardHome) {
        rival.setOnClickListener {
            rival.imageView.visibility = View.INVISIBLE
            rival.name.visibility = View.INVISIBLE
            window.decorView.rootView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            Timer().schedule(11) {
                runOnUiThread {
                    rival.imageView.visibility = View.VISIBLE
                    rival.name.visibility = View.VISIBLE
                }
            }
        }
        rival.imageView.alpha = 0.5F
        rival.name.alpha = 0.5F
    }

   fun setChallenge(rival: CardHome){
       rival.imageView.alpha = 1F
       rival.name.alpha = 1F
       rival.setOnClickListener {
           val dialogChallenge: DialogChallenge = DialogChallenge(this, playerSelf,  rival.playerRival, null,"INVITATION")
           dialogChallenge.show()
       }
   }

    fun setRivals() {
        val listOngoing: List<EntityGame> = listMenu.filter { it.status == "ONGOING" }
        for(game: EntityGame in listOngoing){
            val playerOther: EntityPlayer = game.getPlayerOther(playerSelf.username)
            val usernameOther: String = playerOther.username
            val usernameRival0: String = rival0.name.text.toString()
            if(usernameOther == usernameRival0){
                this.setShudder(rival0)
            } else {
                this.setChallenge(rival0)
            }
            val usernameRival1: String = rival1.name.text.toString()
            if(usernameOther == usernameRival1){
                this.setShudder(rival1)
            } else {
                this.setChallenge(rival1)
            }
            val usernameRival2: String = rival2.name.text.toString()
            if(usernameOther == usernameRival2){
                this.setShudder(rival2)
            } else {
                this.setChallenge(rival2)
            }
        }
    }

    private fun getRivals() {
        this.rival0 = findViewById(R.id.rival_0)
        rival0.visibility = View.INVISIBLE
        this.rival1 = findViewById(R.id.rival_1)
        rival1.visibility = View.INVISIBLE
        this.rival2 = findViewById(R.id.rival_2)
        rival2.visibility = View.INVISIBLE

        val url = "${ServerAddress().IP}:8080/player/rivals/${this.playerSelf.id}"
        val request = JsonArrayRequest(
            Request.Method.POST, url, null,
            { response: JSONArray ->
                val playerRival0: EntityPlayer = parsePlayer.execute(response.getJSONObject(0))
                rival0.playerRival = playerRival0
                rival0.name.text = playerRival0.username
                glide.load(playerRival0.avatar).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        rival0.imageView.setImageDrawable(resource)
                        rival0.visibility = View.VISIBLE
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
                val playerRival1: EntityPlayer = parsePlayer.execute(response.getJSONObject(1))
                rival1.playerRival = playerRival1
                rival1.name.text = playerRival1.username
                glide.load(playerRival1.avatar).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        rival1.imageView.setImageDrawable(resource)
                        rival1.visibility = View.VISIBLE
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
                val playerRival2: EntityPlayer = parsePlayer.execute(response.getJSONObject(2))
                rival2.playerRival = playerRival2
                rival2.name.text = playerRival2.username
                glide.load(playerRival2.avatar).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        rival2.imageView.setImageDrawable(resource)
                        rival2.visibility = View.VISIBLE
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
                this.setRivals()
            },
            {
            }
        )
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(this).addToRequestQueue(request)
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
                    this.arrayAdapter.notifyDataSetChanged()
                    this.progressBar.visibility = View.INVISIBLE
                    this.swipeRefreshLayout.isRefreshing = false

                    this.setRivals()
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

    fun dialogRematch(playerSelf: EntityPlayer, playerOther: EntityPlayer, game: EntityGame?, action: String = "INVITATION") {
        val dialogRematch: DialogChallenge = DialogChallenge(this, playerSelf, playerOther, game, action)
        dialogRematch.show()
    }

    private fun setScrollListener(listView: ListView) {
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                val position: Int = listView.lastVisiblePosition
                val countHeader: Int = listView.headerViewsCount
                val countFooter: Int = listView.footerViewsCount
                val visible: Int = position - countHeader - countFooter
                val bottom: Boolean =  visible >= arrayAdapter.count - 1
                val idle: Boolean = scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                if (bottom && idle) {
                    fetchGames()
                }
            }
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {}
        })
    }

    private fun setTabListener(tabLayout: TabLayout) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                extendedDataHolder.putExtra("player_self", playerSelf)
                when (tab.position) {
                    0 -> {
                        val title: String = "⚡ hang tight ⚡"
                        val message: String = "single player mode coming soon! \uD83E\uDD16"
                        DialogOk(applicationContext).render(title, message)
                    }
                    1 -> {
                        val intent = Intent(applicationContext, ActivityConfig::class.java)
                        startIntent(intent)
                    }
                    2 -> {
                        val intent = Intent(applicationContext, ActivityLeaderboard::class.java)
                        startIntent(intent)
                    }
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })
    }

    private fun startIntent(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        applicationContext.startActivity(intent)
    }

    override fun onBackPressed() {
        this.extendedDataHolder.putExtra("player_self", playerSelf)
        val intent = Intent(applicationContext, ActivityProfile::class.java)
        this.startIntent(intent)
    }
}

