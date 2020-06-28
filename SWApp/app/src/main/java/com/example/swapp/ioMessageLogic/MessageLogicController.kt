package com.example.swapp.ioMessageLogic

import android.util.Log
import com.example.swapp.ioMessageLogic.income.DataMessage
import com.example.swapp.ioMessageLogic.income.IncomeMessage
import com.example.swapp.ioMessageLogic.income.LogicMessage
import com.google.gson.Gson
import java.lang.Exception

class MessageLogicController(var gson: Gson) {
    var state : Int = 0;


    fun handleIncome(string: String) : String?{
        var incomeMessage : IncomeMessage? = null
        when (state){
            1 -> {
                try {
                    incomeMessage = gson.fromJson(string, DataMessage::class.java) as DataMessage
                    state = 0
                    Log.i("type ", "map")
                } catch (e : Exception){}
            }
            0 -> {
                incomeMessage = gson.fromJson(string, LogicMessage::class.java) as LogicMessage
                if (incomeMessage.type == "confirm_subscription") {
                    state = 1;
                }
                Log.i("type ","logic");
                return null
            }
        }

        try {
            var data: ArrayList<ArrayList<String?>> =
                trim((incomeMessage as DataMessage).message!!.map as String)
        } catch (e : Exception){}

        return ""
    }

    fun trim(input : String): ArrayList<ArrayList<String?>>{
        var data = ""
        data = input.removePrefix("[[")
        data = data.removeSuffix("]]")

        val res : ArrayList<ArrayList<String?>> = ArrayList()

        var lines = data.split("],[")
        for (line in lines){
            res.add(
                line.split(",") as java.util.ArrayList<String?>
            )
        }
        return res
    }
}