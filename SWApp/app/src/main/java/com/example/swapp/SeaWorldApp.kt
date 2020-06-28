package com.example.swapp

import android.app.Application
import android.util.Log
import com.example.swapp.webSocket.WebSocketConnection

class SeaWorldApp : Application() {
    var webSocketConnection : WebSocketConnection? = null

    fun init(url : String) : WebSocketConnection {
        webSocketConnection =
            WebSocketConnection(url)
        openSocketConnection()
        BackgroundManager(this).registerListener(appActivityListener)
        return webSocketConnection!!
    }

    fun closeSocketConnection(){
        webSocketConnection!!.closeConnection()
    }
    fun openSocketConnection(){
        webSocketConnection!!.openConnection()
    }
    fun isSocketConnected() : Boolean {
        return webSocketConnection!!.isConnected()
    }
    fun reconnect(){
        return webSocketConnection!!.openConnection()
    }

    private val appActivityListener: BackgroundManager.Listener = object : BackgroundManager.Listener{
        override fun onBecameForeground() {
            openSocketConnection()
//            Log.i("Websocket", "Became Foreground")
        }
        override fun onBecameBackground() {
            closeSocketConnection()
//            Log.i("Websocket", "Became Background")
        }
    }

}