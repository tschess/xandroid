package io.tschess.tschess.home


import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.SkuDetailsParams
import com.android.volley.Request
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.config.ActivityConfig
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.leaderboard.ActivityLeaderboard
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.profile.ActivityProfile
import io.tschess.tschess.purchase.DialogPurchase
import io.tschess.tschess.server.CustomJsonArrayRequest
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import kotlinx.android.synthetic.main.card_home.view.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule


class ActivityHome : AppCompatActivity(), Refresher, Rival, SwipeRefreshLayout.OnRefreshListener {

    private var size: Int
    private var index: Int
    private var fetched: Boolean
    private val parseGame: ParseGame
    private var listMenu: ArrayList<EntityGame>

    private lateinit var playerSelf: EntityPlayer
    private lateinit var extendedDataHolder: ExtendedDataHolder

    init {
        this.size = 9
        this.index = 0
        this.fetched = false
        this.parseGame = ParseGame()
        this.listMenu = arrayListOf()
    }

    lateinit var progressBar: ProgressBar
    lateinit var arrayAdapter: AdapterHome
    lateinit var utilityRival: UtilityRival
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

        this.extendedDataHolder = ExtendedDataHolder().getInstance()
        if(extendedDataHolder.hasExtra("player_self")){
            this.playerSelf = extendedDataHolder.getExtra("player_self") as EntityPlayer
            this.extendedDataHolder.clear()
        }

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)
        headerSelf.setListenerProfile(this.playerSelf)

        /* * */
        val rival0: CardHome  = findViewById(R.id.rival_0)
        val rival1: CardHome = findViewById(R.id.rival_1)
        val rival2: CardHome = findViewById(R.id.rival_2)
        this.utilityRival = UtilityRival(rival0, rival1, rival2, this.playerSelf, this, this)
        /* * */

        this.listMenu = arrayListOf()
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

                    this.utilityRival.setRivalList(listMenu)
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

    // TODO: Purchase vs. Challenge
    fun dialogRematch(
        playerSelf: EntityPlayer,
        playerOther: EntityPlayer,
        game: EntityGame?,
        action: String = "INVITATION"
    ) {
        val client = BillingClient.newBuilder(application)
            .enablePendingPurchases()
            .setListener { _, _ -> }
            .build()
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    client.queryPurchaseHistoryAsync(
                        BillingClient.SkuType.SUBS
                    ) { _, purchasesList ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            //Log.e("purchasesList", "${purchasesList}")
                            //Log.e("BillingClient", String.format("List size is %d", purchasesList?.size ?: -1))
                            if(purchasesList?.size == 0){
                                dialogPurchase(playerOther, game, action)
                                return@queryPurchaseHistoryAsync
                            }
                            //else {
                            dialogChallenge(playerOther, game, action)
                            //}
                        }
                    }
                    return
                }
                dialogPurchase(playerOther, game, action)
            }
            override fun onBillingServiceDisconnected() {
                dialogPurchase(playerOther, game, action)
            }
        })
    }

    fun dialogPurchase(playerOther: EntityPlayer, game: EntityGame?, action: String) {
        val dialogPurchase: DialogPurchase = DialogPurchase(
            this,
            playerSelf,
            playerOther,
            game,
            action,
            refresher = this
        )
        dialogPurchase.activity = this
        dialogPurchase.show()
    }

    fun dialogChallenge(playerOther: EntityPlayer, game: EntityGame?, action: String) {
        val dialogRematch: DialogChallenge = DialogChallenge(
            this,
            playerSelf,
            playerOther,
            game,
            action,
            refresher = this
        )
        dialogRematch.show()
    }


    private fun setScrollListener(listView: ListView) {
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                val position: Int = listView.lastVisiblePosition
                val countHeader: Int = listView.headerViewsCount
                val countFooter: Int = listView.footerViewsCount
                val visible: Int = position - countHeader - countFooter
                val bottom: Boolean = visible >= arrayAdapter.count - 1
                val idle: Boolean = scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                if (bottom && idle) {
                    fetchGames()
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
    }

    fun dialogSinglePlayer() {
        val title: String = "⚡ Hang tight ⚡"
        val message: String = "Single-player mode coming soon! \uD83E\uDD16"
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton("Ok") { dialog, _ ->
            dialog.cancel()
        }
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
    }

    private fun setTabListener(tabLayout: TabLayout) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                extendedDataHolder.putExtra("player_self", playerSelf)
                when (tab.position) {
                    0 -> {
                        dialogSinglePlayer()
                    }
                    1 -> {
                        val intent = Intent(applicationContext, ActivityConfig::class.java)
                        startIntent(intent)
                    }
                    2 -> {
                        val intent = Intent(applicationContext, ActivityLeaderboard::class.java)
                        startIntent(intent)
                    }
                    3 -> {
                        val intent = Intent(applicationContext, ActivityProfile::class.java)
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

    override fun shudder(rival: CardHome) {
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

    override fun challenge(rival: CardHome) {
        rival.imageView.alpha = 1F
        rival.name.alpha = 1F
        rival.setOnClickListener {
            val dialogChallenge: DialogChallenge =
                DialogChallenge(this, playerSelf, rival.playerRival, null, "INVITATION", refresher = this)
            dialogChallenge.show()
        }
    }
}

