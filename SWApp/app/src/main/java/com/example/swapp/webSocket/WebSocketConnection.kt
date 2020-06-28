package com.example.swapp.webSocket

import android.os.Handler
import android.util.Log
import com.example.swapp.ioMessageLogic.income.IncomeMessageEvent
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

class WebSocketConnection(
    val url : String
): ClientWebSocket.MessageListener {
    var clientWebSocket : ClientWebSocket? = null
    val socketConnectionHandler = Handler()
    val checkConnectionRunnable = Runnable {
        if (!clientWebSocket!!.getConnection().isOpen){
            openConnection()
        }
        startCheckConnection()
    }

    private fun startCheckConnection(){
        socketConnectionHandler.postDelayed(
            checkConnectionRunnable, 5000
        )
    }
    private fun stopCheckConnection(){
        socketConnectionHandler.removeCallbacks(
            checkConnectionRunnable
        )
    }

    fun openConnection(){
        if (clientWebSocket != null ) clientWebSocket!!.close()
        try{
            clientWebSocket = ClientWebSocket(
                this,
                url
            )
            clientWebSocket!!.connect()
            Log.i("WebSocket", "Connect")
        } catch (e : Exception){
            e.printStackTrace()
        }
        startCheckConnection()
    }
    fun closeConnection(){
        if (clientWebSocket != null){
            clientWebSocket!!.close()
            clientWebSocket = null
        }
        stopCheckConnection()
    }

    override fun onSocketMessage(message: String) {
        EventBus.getDefault().post(
            IncomeMessageEvent(
                message
            )
        )
    }
    fun isConnected() : Boolean{
        return clientWebSocket != null &&
                clientWebSocket!!.getConnection() != null &&
                clientWebSocket!!.getConnection().isOpen;
    }

    fun send(message: String){
        clientWebSocket!!.send(message)
    }
}