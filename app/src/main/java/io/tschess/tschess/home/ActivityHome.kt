package io.tschess.tschess.home


import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import io.tschess.tschess.R
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.model.*

class ActivityHome : AppCompatActivity() {

    private var size: Int = 9
    private var index: Int = 0
    private var fetched: Boolean = false
    private val parseGame: ParseGame = ParseGame()
    private val parsePlayer: ParsePlayer = ParsePlayer()

    lateinit var progressBar: ProgressBar
    private lateinit var playerSelf: EntityPlayer
    private lateinit var adapterMenu: AdapterHome
    private lateinit var listMenu: ArrayList<EntityGame>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        this.progressBar = findViewById(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        extras.clear()

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)

    }


}

@FunctionalInterface
interface Refresher {
    fun refresh(position: Int)
}