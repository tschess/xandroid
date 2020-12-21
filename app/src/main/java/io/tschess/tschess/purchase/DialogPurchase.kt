package io.tschess.tschess.purchase

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import com.android.billingclient.api.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.home.Refresher
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import io.tschess.tschess.tschess.ActivityTschess
import org.json.JSONObject

class DialogPurchase(
    context: Context,
    val playerSelf: EntityPlayer,
    val playerOther: EntityPlayer,
    val game: EntityGame? = null,
    val action: String = "INVITATION",
    val refresher: Refresher? = null
) : Dialog(context) {

    lateinit var activity: Activity

    lateinit var progressBar: ProgressBar
    private val parseGame: ParseGame = ParseGame()

    private val purchasesUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }

    private var billingClient = BillingClient.newBuilder(getContext())
        .setListener(purchasesUpdateListener)
        .enablePendingPurchases()
        .build()


    fun setOriginal() {

    }


    // val textTitle: TextView = findViewById(R.id.text_title)
    //        val textInfo: TextView = findViewById(R.id.text_info)
    //        val textConfig: TextView = findViewById(R.id.config_text)
    //        val picker: NumberPicker = findViewById<View>(R.id.number_picker) as NumberPicker
    //        val textSend: TextView = findViewById(R.id.text_send)
    //        val textSubscribe: TextView = findViewById(R.id.text_subscribe)
    fun setSubscribe(
        textTitle: TextView,
        textConfig: TextView,
        picker: NumberPicker,
        textSend: TextView,
        textSubscribe: TextView
    ) {
        textTitle.alpha = 0.5F

        textConfig.alpha = 0.5F
        picker.alpha = 0.5F
        picker.isEnabled = false

        textSubscribe.text = "$0.99 × Month 📅"
        textSubscribe.alpha = 0.9F
        textSend.text = "$5.99 × Year 🍂❄️🌷\uD83C\uDF1E️"
        textSend.alpha = 0.9F
        textSend.setTextColor(Color.WHITE)

        textSubscribe.setOnClickListener {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        //querySkuDetailsMonth()
                        querySkuDetails(0) // × Month
                    }
                }
                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }
    }




    fun querySkuDetails(index: Int) {
        val skuList = ArrayList<String>()
        skuList.add("001")
        skuList.add("002")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            // Process the result.
            Log.e("billingResult", "${billingResult}")
            Log.e("skuDetailsList", "${skuDetailsList}")

            val skuDetails: SkuDetails = skuDetailsList!![index]
            // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams).responseCode
            Log.e("responseCode", "\n\n\n${responseCode}\n\n\n")
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_purchase)

        val textTitle: TextView = findViewById(R.id.text_title)
        val textInfo: TextView = findViewById(R.id.text_info)
        val textConfig: TextView = findViewById(R.id.config_text)
        val picker: NumberPicker = findViewById<View>(R.id.number_picker) as NumberPicker
        val textSend: TextView = findViewById(R.id.text_send)
        val textSubscribe: TextView = findViewById(R.id.text_subscribe)
        textSubscribe.setOnClickListener {

            setSubscribe(
                textTitle,
                textConfig,
                picker,
                textSend,
                textSubscribe)
            }







        this.progressBar = findViewById(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE




        textTitle.text = "\uD83E\uDD1C vs. ${playerOther.username} \uD83E\uDD1B"


        textInfo.text = "Subscribe to unlock all selections."


        textConfig.text = "Selection:"

        val listOption: MutableList<String> = mutableListOf(
            "Config. 0 \uD83D\uDD12",
            "Config. 1 \uD83D\uDD12",
            "Config. 2 \uD83D\uDD12",
            "    Chess    ",
            "    I'm Feelin' Lucky    "
        )
        if (action == "ACCEPT") {
            textSend.text = "\uD83C\uDF89 Let's play! \uD83C\uDF89"
        }

        picker.minValue = 0
        picker.maxValue = listOption.size - 1
        picker.wrapSelectorWheel = true
        picker.displayedValues = listOption.toTypedArray()
        picker.value = 0


        textSend.setOnClickListener {
            if(textSend.text == "⚡ Send invite ⚡"){
                this.progressBar.visibility = View.VISIBLE
                if (action == "ACCEPT") {
                    accept(picker.value, game!!.id)
                    return@setOnClickListener
                }
                if (action == "REMATCH") {
                    val white: Boolean = game!!.getWhite(playerSelf.username)
                    rematch(picker.value, white)
                    return@setOnClickListener
                }
                //"INVITATION"
                invitation(picker.value)
                return@setOnClickListener
            }
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        querySkuDetails(1)  // × Year
                    }
                }
                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })

        }
    }

    fun rematch(config: Int, white: Boolean) {
        val url: String = "${ServerAddress().IP}:8080/game/rematch"

        val params: MutableMap<String, String> = mutableMapOf()
        params["id_self"] = playerSelf.id
        params["id_other"] = playerOther.id
        params["config"] = config.toString()
        params["white"] = white.toString()

        val listenerResponse: Response.Listener<JSONObject> = Response.Listener {
            this.progressBar.visibility = View.INVISIBLE
            //DialogOk(context).confirm(playerOther.username)
            val title: String = "✅ Success \uD83D\uDC4C"
            val message: String = "Challenge dispatched to ${playerOther.username} ♟️"
            //render(title, message)
            val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialog)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton("Ok") { dialog, _ ->
                dialog.cancel()
            }
            val alert: AlertDialog = dialogBuilder.create()
            alert.show()
            refresher!!.refresh()
            dismiss()
        }

        execute(url, listenerResponse, this.getError("Challenge wasn't delivered."), params)
    }

    fun accept(config: Int, game_id: String) {
        val url: String = "${ServerAddress().IP}:8080/game/ack"

        val params: MutableMap<String, String> = mutableMapOf()
        params["id_self"] = playerSelf.id
        params["id_game"] = game_id
        params["config"] = config.toString()

        val listenerResponse: Response.Listener<JSONObject>? = Response.Listener { response ->
            this.progressBar.visibility = View.INVISIBLE

            val game: EntityGame = parseGame.execute(response)

            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("game", game)
            extras.putExtra("player_self", playerSelf)

            val intent = Intent(context, ActivityTschess::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

            dismiss()
        }
        execute(url, listenerResponse, this.getError("Please try again later."), params)
    }

    fun invitation(config: Int) {
        val url: String = "${ServerAddress().IP}:8080/game/challenge"

        val params: MutableMap<String, String> = mutableMapOf()
        params["id_self"] = playerSelf.id
        params["id_other"] = playerOther.id
        params["config"] = config.toString()

        val listenerResponse: Response.Listener<JSONObject> = Response.Listener {
            this.progressBar.visibility = View.INVISIBLE
            //DialogOk(context).confirm(playerOther.username)

            val title: String = "✅ Success \uD83D\uDC4C"
            val message: String = "Challenge dispatched to ${playerOther.username} ♟️"
            //render(title, message)
            val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialog)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton("Ok") { dialog, _ ->
                dialog.cancel()
            }
            val alert: AlertDialog = dialogBuilder.create()
            alert.show()

            refresher!!.refresh()
            dismiss()
        }
        execute(url, listenerResponse, this.getError("Challenge wasn't delivered."), params)
    }

    fun getError(message: String): Response.ErrorListener {
        return Response.ErrorListener {
            progressBar.visibility = View.INVISIBLE
            //DialogOk(context).error("something went wrong! $message")
            val title: String = "❌ error ✋"
            val message: String = "Something went wrong! $message"
            val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialog)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton("Ok") { dialog, _ ->
                dialog.cancel()
            }
            val alert: AlertDialog = dialogBuilder.create()
            alert.show()
            dismiss()
        }
    }

    fun execute(
        url: String?,
        listenerResponse: Response.Listener<JSONObject>?,
        listenerError: Response.ErrorListener?,
        params: MutableMap<String, String>
    ) {
        val jsonObject: JSONObject = JSONObject(params as Map<*, *>)
        val jsonObjectRequest: JsonObjectRequest =
            JsonObjectRequest(Request.Method.POST, url!!, jsonObject, listenerResponse!!, listenerError!!)
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }

}

