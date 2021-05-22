package com.muod.calldiary.callback

import com.muod.calldiary.model.CallObject

interface DoneCallback {
    fun refreshItems()
    fun doneItem(item:CallObject)
    fun unDoneItem(item:CallObject)
    fun editItem(id:Long)
    fun getContactName(number:String):String
}