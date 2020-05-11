package com.mobbile.paul.util

class ChatMessages(val key:String, val msg: String, val dates: String, val timeago: String){
    constructor() : this("","","","")
}

class BargeMessages(val uid:String)
