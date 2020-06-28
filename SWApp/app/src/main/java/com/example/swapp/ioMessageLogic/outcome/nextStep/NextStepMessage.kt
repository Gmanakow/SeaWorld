package com.example.swapp.ioMessageLogic.outcome.nextStep

import com.google.gson.Gson

class NextStepMessage(
    var gson: Gson,
    id : String
)  {

    var message = gson.toJson(
        Step(id)

    )
    inner class Step(
        id : String
    ) {

        var command = "message"
        var data = gson.toJson(
            Action()
        )

        inner class Action {
            var action = "next_step"
        }

        var identifier = gson.toJson(
            Data(id)
        )

        inner class Data(id: String) {
            var channel = "WorldChannel"
            var id = id
        }
    }
}