package io.tschess.tschess.config

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.edit.ActivityEdit
import io.tschess.tschess.gameboard.BoardConfig
import io.tschess.tschess.header.HeaderSelf
//import io.tschess.tschess.edit.ActivityEditSelf
import io.tschess.tschess.model.EntityPlayer
//import io.tschess.tschess.fairy.ActivityFairy
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.model.ExtendedDataHolder

class ActivityConfig : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    private lateinit var playerSelf: EntityPlayer

    override fun onBackPressed() {
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        extras.clear()

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)

        val configView00: BoardConfig = findViewById(R.id.config_00)
        val config00: Array<Array<Piece?>> = this.playerSelf.getConfig(0)

        configView00.populateBoard(config00)
        configView00.setOnClickListener(View.OnClickListener {
            // Do some work here
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("config", 0)
            val intent = Intent(applicationContext, ActivityEdit::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            applicationContext.startActivity(intent)
            finish()
        })

        val configView01: BoardConfig = findViewById(R.id.config_01)
        val config01: Array<Array<Piece?>> = this.playerSelf.getConfig(1)

        configView01.populateBoard(config01)
        configView01.setOnClickListener(View.OnClickListener {
            // Do some work here
            val intent = Intent(applicationContext, ActivityEdit::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("config", 1)
            applicationContext.startActivity(intent)
            finish()
        })

        val configView02: BoardConfig = findViewById(R.id.config_02)
        val config02: Array<Array<Piece?>> = this.playerSelf.getConfig(2)

        configView02.populateBoard(config02)
        configView02.setOnClickListener(View.OnClickListener {
            // Do some work here
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("config", 2)
            val intent = Intent(applicationContext, ActivityEdit::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            applicationContext.startActivity(intent)
            finish()
        })

        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                //extras.putExtra("player_self", playerSelf)
                //val intent = Intent(applicationContext, ActivityFairy::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                //startActivity(intent)
                //finish()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })
        tabLayout.selectTab(null)


    }


}
