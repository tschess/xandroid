package io.tschess.tschess.fairy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.config.ActivityConfig
import io.tschess.tschess.dialog.DialogFairy
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.piece.fairy.Fairy
import io.tschess.tschess.piece.fairy.amazon.Amazon
import io.tschess.tschess.piece.fairy.hunter.Hunter
import io.tschess.tschess.piece.fairy.poison.Poison

class ActivityFairy : AppCompatActivity() {

    private val fairyList: Array<Fairy> = arrayOf(Poison(), Amazon(), Hunter())
    lateinit var progressBar: ProgressBar
    lateinit var playerSelf: EntityPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        extras.clear()

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)

        val adapterHome: AdapterFairy = AdapterFairy(this, fairyList)
        val listView: ListView = findViewById(R.id.list_view)
        listView.adapter = adapterHome
        listView.setOnItemClickListener { parent, view, position, id ->

            val fairy: Fairy = fairyList[position]
            val name: String = fairy.name

            val dialogFairy: DialogFairy = DialogFairy(this, name)
            dialogFairy.show()
            //val intent = Intent(this, ActivityDetail::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)


            //intent.putExtra("fairy_name", name)
            //val image: Int = fairy.imageDefault
            //intent.putExtra("fairy_image", image)
            //val strength: Int = fairy.strength
            //intent.putExtra("fairy_strength", strength)
            //val describe: String = fairy.describe
            //intent.putExtra("fairy_describe", describe)

            //extras.putExtra("player_self", playerSelf)
            //startActivity(intent)
            //finish()
        }

        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                        extras.putExtra("player_self", playerSelf)
                        finish()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })
    }

    override fun onBackPressed() {
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)
        val intent = Intent(applicationContext, ActivityConfig::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        applicationContext.startActivity(intent)
        finish()
    }
}