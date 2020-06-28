package com.example.swapp.webSocket

import android.util.Log
import com.neovisionaries.ws.client.*
import java.io.IOException
import javax.net.ssl.SSLContext

class ClientWebSocket(
    val listener : MessageListener,
    val host : String
){
    var webSocket : WebSocket? = null

    fun connect(){
        Thread (Runnable{
            if (webSocket != null){
                reconnect()
            } else{
                    val factory = WebSocketFactory()
                    val context: SSLContext = CustomSslContext()
                        .getInstance("TLS")
                    factory.setSSLContext(context)
                    webSocket = factory.createSocket(host)
                    webSocket!!.addListener(SocketListener())
                    webSocket!!.connect()
            }
        }).start()
    }
    fun reconnect(){
        try{
            webSocket = webSocket!!.recreate().connect()
        } catch (e : WebSocketException){
            e.printStackTrace()
        } catch (e : IOException){
            e.printStackTrace()
        }
    }
    fun getConnection() : WebSocket{
        return webSocket!!
    }
    fun close(){
        webSocket!!.disconnect()
    }

    fun send(message: String){
        Log.i("TAAAAAAAAAAAAAG", message)
        webSocket!!.sendText(message)
    }

    inner class SocketListener : WebSocketAdapter() {
        val TAG = "Websocket"

        override fun onConnected(
            websocket: WebSocket?,
            headers: MutableMap<String, MutableList<String>>?
        ) {
            super.onConnected(websocket, headers)
            Log.i(TAG, "onConnected")
        }

        override fun onTextMessage(websocket: WebSocket?, message : String) {
            listener.onSocketMessage(message)
//            Log.i(TAG, "Message --> " + message)
        }

        override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
            Log.i(TAG, "Error --> " + cause?.message )
            reconnect()
        }

        override fun onDisconnected(
            websocket: WebSocket?,
            serverCloseFrame: WebSocketFrame?,
            clientCloseFrame: WebSocketFrame?,
            closedByServer: Boolean
        ) {
            Log.i(TAG, "onDisconnected")
            if (closedByServer){
                reconnect()
            }
        }

        override fun onUnexpectedError(websocket: WebSocket?, cause: WebSocketException?) {
            Log.i(TAG, "Error --> " + cause?.message)
            reconnect()
        }

        override fun onPongFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
            super.onPongFrame(websocket, frame)
            websocket?.sendPing("Are you there?")
        }
    }

    interface MessageListener {
        fun onSocketMessage(message: String)
    }

}