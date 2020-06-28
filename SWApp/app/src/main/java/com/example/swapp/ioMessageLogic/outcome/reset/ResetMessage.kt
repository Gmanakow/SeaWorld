package com.example.swapp.ioMessageLogic.outcome.reset

import com.google.gson.Gson

class ResetMessage(
    var gson: Gson,
    id : String
) {

    var message = gson.toJson(
        Reset(id)
    )

    inner class Reset(
        id : String
    ) {
        var command = "message"
        var data = gson.toJson(
            Action()
        )
        inner class Action{
            var action = "reset"
        }

        var identifier = gson.toJson(
            Data(id)
        )
        inner class Data(id: String){
            var channel = "WorldChannel"
            var id = id
        }
    }

}