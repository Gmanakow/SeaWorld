package com.example.swapp.ioMessageLogic.income

class DataMessage : IncomeMessage(){
    var identifier : String? = null

    var message : Data? = null
    inner class Data {
        var map : String? = null
        var id : String? = null
    }
}