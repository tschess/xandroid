package io.tschess.tschess.snapshot

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.header.HeaderSnapshot
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.model.ExtendedDataHolder

class ActivitySnapshot : AppCompatActivity() {

    private lateinit var snapshotView: ViewSnapshot
    private lateinit var matrix: Array<Array<Piece?>>
    private lateinit var game: EntityGame
    private lateinit var playerSelf: EntityPlayer
    private var white: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snapshot)

        //this.playerSelf = intent.extras!!.getParcelable("player_self")!!
        //this.game = intent.extras!!.getParcelable("game")!!


        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        this.game = extras.getExtra("game") as EntityGame


        val headerSnap: HeaderSnapshot = findViewById(R.id.header)
        headerSnap.initialize(playerSelf, game)

        extras.clear()

        this.matrix = game.getMatrix(playerSelf.username)
        this.white = game.getWhite(playerSelf.username)

        //val playerOther: PlayerEntity = game.getPlayerOther(this.playerSelf.username)


        val textViewWhite: TextView = findViewById(R.id.username_white)
        textViewWhite.text = game.white.username
        val textViewBlack: TextView = findViewById(R.id.username_black)
        textViewBlack.text = game.black.username

        val textViewDate: TextView = findViewById(R.id.date)
        textViewDate.text = game.getDate()

        this.snapshotView = findViewById(R.id.board_view)

        this.snapshotView.populateBoard(this.matrix)

        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        extras.putExtra("player_self", playerSelf)
                        finish()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })
    }
}