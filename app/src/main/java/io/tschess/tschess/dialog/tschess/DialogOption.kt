package io.tschess.tschess.dialog.tschess

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
import java.util.*

class DialogOption( //TODO: same signature as _DialogDraw_ (consolidate) ...
    val context: Context,
    val playerSelf: EntityPlayer,
    val game: EntityGame,
    var progressBar: ProgressBar
) {


}