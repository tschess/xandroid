package io.tschess.tschess.other

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.model.EntityPlayer

import io.tschess.tschess.server.CustomJsonArrayRequest
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class ActivityOther : AppCompatActivity() {

    private val parseGame: ParseGame = ParseGame()

    lateinit var playerOther: EntityPlayer
    lateinit var playerSelf: EntityPlayer

    lateinit var progressBar: ProgressBar
    private var fetched: Boolean = false


    private var size: Int = 9
    private var index: Int = 0

    private lateinit var adapterOther: AdapterOther
    private lateinit var listOther: ArrayList<EntityGame>
    private lateinit var listView: ListView

    override fun onBackPressed() {
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        this.playerOther = extras.getExtra("player_other") as EntityPlayer
        extras.clear()

        //this.playerSelf = intent.extras!!.getParcelable("player_self")!!
        //this.playerOther = intent.extras!!.getParcelable("player_other")!!

        this.listOther = arrayListOf()
        this.adapterOther = AdapterOther(this.playerOther, this, this.listOther)
        this.listView = findViewById(R.id.list_view)
        this.listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && listView.lastVisiblePosition - listView.headerViewsCount -
                    listView.footerViewsCount >= listOther.size - 1
                ) {
                    fetchGames() // now your listView has hit the bottom
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
        this.listView.adapter = adapterOther
        this.fetchGames()

        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                //val intent = Intent(applicationContext, ActivityQuick::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                //val config: Int = (0..3).random()
                //extras.putExtra("config", config)
                //extras.putExtra("player_self", playerSelf)
                //extras.putExtra("player_other", playerOther)
                //extras.putExtra("action", "OTHER")
                //applicationContext.startActivity(intent)


                val dialogChallenge: DialogChallenge = DialogChallenge(applicationContext, playerSelf, playerOther, null, "INVITATION")
                dialogChallenge.show()


            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })

        val headerOther: HeaderSelf = findViewById(R.id.header)
        headerOther.initialize(playerOther)

    }

    private fun fetchGames() {
        if (fetched) {
            return
        }
        this.progressBar.visibility = View.VISIBLE

        val url = "${ServerAddress().IP}:8080/game/menu"
        val params = HashMap<String, Any>()
        params["id"] = playerOther.id!!
        params["index"] = this.index.toString()
        params["size"] = this.size.toString()
        params["self"] = false
        val jsonObject = JSONObject(params as Map<*, *>)
        val request =
            CustomJsonArrayRequest(
                Request.Method.POST, url, jsonObject,
                { response ->

                    for (i: Int in 0 until response.length()) {
                        //val game: EntityGame =
                        //Gson().fromJson(response.getJSONObject(i).toString(), EntityGame::class.java)!!

                        val jsonObject: JSONObject = response.getJSONObject(i)
                        val game: EntityGame = parseGame.execute(jsonObject)
                        this.listOther.add(game)
                        //Log.e("-->", "${game.getDate()}")
                    }
                    this.adapterOther.notifyDataSetChanged()
                    this.progressBar.visibility = View.INVISIBLE
                },
                {
                    fetched = true
                    this.progressBar.visibility = View.INVISIBLE
                    Log.e("error in volley request", "${it.message}")
                })
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(this).addToRequestQueue(request)
        this.index += 1
    }

}
