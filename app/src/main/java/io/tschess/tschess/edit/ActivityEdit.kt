package io.tschess.tschess.edit

import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.config.ActivityConfig
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.dialog.DialogFairy
import io.tschess.tschess.gameboard.BoardEdit
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.model.Render
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class ActivityEdit : AppCompatActivity(), View.OnLongClickListener, View.OnDragListener, View.OnTouchListener {

    private val parsePlayer: ParsePlayer = ParsePlayer()

    lateinit var progressBar: ProgressBar
    private lateinit var editView: BoardEdit
    private lateinit var matrix: Array<Array<Piece?>>
    lateinit var playerSelf: EntityPlayer
    var config: Int = 0

    lateinit var labelInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        this.labelInfo = findViewById(R.id.info)
        this.labelInfo.visibility = View.INVISIBLE



        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val body: LinearLayout = findViewById(R.id.body)
        body.setOnDragListener { view, event -> onDrag(view, event) }

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        this.config = extras.getExtra("config") as Int
        extras.clear()

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(playerSelf, "config $config")

        this.editView = findViewById(R.id.edit_view)
        this.editView.listenerDrop = this
        this.editView.listenerClick = this
        this.matrix = playerSelf.getConfig(config)
        this.editView.populateBoard(this.matrix)
        val unallocated: Int = this.unallocated(this.matrix)
        this.setFeedbackCard(unallocated)



        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        onBackPressed()
                    }
                    1 -> {
                        renderMenuHelp()
                    }
                    2 -> {
                        persistToServer()
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, ActivityConfig::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)
        applicationContext.startActivity(intent)
        finish()
    }

    fun persistToServer() {
        progressBar.visibility = View.VISIBLE
        val id: String = this.playerSelf.id!!
        val index: String = this.config.toString()
        val matrix: List<List<String>> = this.playerSelf.setConfig(this.matrix)

        val url = "${ServerAddress().IP}:8080/player/config"
        val params = HashMap<String, Any>()
        params["id"] = id
        params["index"] = index
        params["config"] = matrix
        val jsonObject = JSONObject(params as Map<*, *>)
        val request =
            JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response: JSONObject ->
                    this.progressBar.visibility = View.INVISIBLE

                    val playerSelf00: EntityPlayer = parsePlayer.execute(response)

                    val intent = Intent(applicationContext, ActivityConfig::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                    val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
                    extras.putExtra("player_self", playerSelf00)

                    applicationContext.startActivity(intent)
                    finish()
                },
                {
                    this.progressBar.visibility = View.INVISIBLE
                    Log.e("error in volley request", "${it.message}")
                })
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val parent: View = view.parent as View
        parent.visibility = View.INVISIBLE
        view.rootView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        Timer().schedule(11) {
            runOnUiThread {
                parent.visibility = View.VISIBLE
            }
        }
        return false
    }

    fun renderMenuHelp() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle("\uD83E\uDDE9 edit config")
        dialogBuilder.setMessage(
            "long touch on piece to engage \uD83D\uDC47\n" +
                    "\uD83D\uDC49 drag to destination\n" +
                    "or off board to remove \uD83D\uDCA8"
        )

            .setNeutralButton("ok", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
        alert.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.WHITE)
    }

    private fun setFeedbackCard(unallocated: Int) {
        val linearLayout: LinearLayout = findViewById(R.id.container_card)
        //if (unallocated == 0) {
            //linearLayout.visibility = View.INVISIBLE
            //return
        //}
        linearLayout.visibility = View.VISIBLE
        val count: Int = linearLayout.childCount
        for (index: Int in 0 until count) {
            val cardView: CardEdit = linearLayout.getChildAt(index) as CardEdit
            //cardView.labelInfo = labelInfo
            cardView.setLabelInfo(labelInfo)
            //
            if (unallocated - cardView.strength >= 0) {
                cardView.imageView.alpha = 1.0F
                cardView.textViewName.alpha = 1.0F
                cardView.textViewStrength.alpha = 1.0F
                cardView.imageView.setOnTouchListener(cardView.listenerDefault)
            } else {
                cardView.imageView.alpha = 0.5F
                cardView.textViewName.alpha = 0.5F
                cardView.textViewStrength.alpha = 0.5F
                cardView.imageView.setOnTouchListener(this)
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        val index: Int = this.editView.indexOfChild(view.parent as View)
        val row: Int = index / 8
        val col: Int = index % 8

        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        val data: ClipData = ClipData.newPlainText("name", this.matrix[row][col]!!.name)
        val item: ClipData.Item = ClipData.Item("${row}${col}")
        data.addItem(item)

        if(!this.matrix[row][col]!!.chess){
            this.labelInfo.visibility = View.VISIBLE
        } else {
            this.labelInfo.visibility = View.INVISIBLE
        }
        view.startDrag(data, ScaledDragShadow(view, 2F), view, View.DRAG_FLAG_OPAQUE)
        return this.update(listOf(row, col))
    }

    override fun onDrag(view: View?, event: DragEvent?): Boolean {
        if (event!!.action != DragEvent.ACTION_DROP) {
            return true
        }
        /* * */
        this.labelInfo.visibility = View.INVISIBLE
        /* * */

        val clipData: ClipData = event.clipData
        val name: String = clipData.getItemAt(0).text.toString()
        val string: String = clipData.getItemAt(1).text.toString()

        val pointDrop: Point = DragUtility().getTouchPositionFromDragEvent(view!!, event)!!
        val infoDrop: Boolean = DragUtility().isTouchInsideOfView(this.labelInfo, pointDrop)
        if (infoDrop) {
            if(DialogFairy.candidate(name)) {
                val dialogFairy: DialogFairy = DialogFairy(this)
                dialogFairy.show()
            }
        }


        val coord: MutableList<Int> = mutableListOf()
        string.forEach { char ->
            coord.add(char.toString().toInt())
        }
        if (view.parent != this.editView) {
            if (name == "King") {
                val king: Piece = Render(true).getDefault(name)!!
                return this.update(coord, king)
            }
            return true
        }
        val index: Int = this.editView.indexOfChild(view)
        val row: Int = index / 8
        val col: Int = index % 8
        val piece: Piece = Render(true).getDefault(name)!!
        val occupant: Piece = this.matrix[row][col] ?: return this.update(listOf(row, col), piece)
        if (occupant.name == "King") {
            if (string == "99") {
                return false
            }
            return this.update(coord, piece)
        }
        return this.update(listOf(row, col), piece)
    }

    private fun update(coord: List<Int>, piece: Piece? = null): Boolean {
        this.matrix[coord[0]][coord[1]] = piece
        this.editView.populateBoard(this.matrix)
        val unallocated: Int = this.unallocated(this.matrix)
        this.setFeedbackCard(unallocated)
        return true
    }

    private fun unallocated(matrix: Array<Array<Piece?>>): Int {
        var strength: Int = 39
        for (row: Int in 0..1) {
            for (column: Int in 0..7) {
                if (matrix[row][column] == null) {
                    continue
                }
                strength -= matrix[row][column]!!.strength
            }
        }
        val textViewIndicator: TextView = findViewById(R.id.indicator)
        textViewIndicator.text = "unallocated: $strength"
        if (strength == 0) {
            textViewIndicator.alpha = 0.5F
        } else {
            textViewIndicator.alpha = 1F
        }
        return strength
    }



}