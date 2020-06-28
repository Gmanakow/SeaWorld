package com.example.swapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.swapp.ioMessageLogic.MessageLogicController
import com.example.swapp.ioMessageLogic.income.*
import com.example.swapp.ioMessageLogic.outcome.nextStep.NextStepMessage
import com.example.swapp.ioMessageLogic.outcome.reset.ResetMessage
import com.example.swapp.ioMessageLogic.outcome.subUnsub.SubscribeMessage
import com.example.swapp.webSocket.WebSocketConnection
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {
    var gson = Gson()
    var webSocketConnection : WebSocketConnection? = null
    var messageLogicController = MessageLogicController(gson)
    val url = "ws://sea-world.sibext.com/cable?access_token=d994fe8e40788ceb4282bb02bab9534fe805b9ca"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        if (webSocketConnection == null) {
            webSocketConnection = (this.application as SeaWorldApp).init(url)
        }
    }

    public override fun onStop(){
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe
    public fun onEvent(incomeMessageEvent: IncomeMessageEvent) {
        Log.i("income message", incomeMessageEvent.message)
        messageLogicController.handleIncome(incomeMessageEvent.message)
    }

    fun actionConnect(view : View){
        if (webSocketConnection != null){
            if (webSocketConnection!!.isConnected()){
                webSocketConnection!!.send(
                    SubscribeMessage(
                        gson,
                        "" + 2
                    )
                        .message
                )
            }
        }
    }
    fun actionPost(view : View){
        if (webSocketConnection != null){
            if (webSocketConnection!!.isConnected()){
                messageLogicController.state = 1
                webSocketConnection!!.send(
                        NextStepMessage(
                            gson,
                            "" + 2
                        ).message
                )
            }
        }
    }
    fun actionReset(view: View){
        if (webSocketConnection != null){
            if (webSocketConnection!!.isConnected()){
                messageLogicController.state = 1
                webSocketConnection!!.send(
                    ResetMessage(
                        gson,
                        "" + 2
                    ).message
                )
            }
        }
    }
}
