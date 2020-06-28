package com.example.swapp.ioMessageLogic.outcome.subUnsub

import com.google.gson.Gson

class SubscribeMessage(
    var gson: Gson,
    id : String
    ) {

    var message = gson.toJson(
        SubscribeClass(id)
    )

    inner class SubscribeClass(
        id : String
    ){
        var command = "subscribe"
        var identifier = gson.toJson(
            Data(id)
        )

        inner class Data(id: String){
            var channel = "WorldChannel"
            var id = id
        }
    }
}