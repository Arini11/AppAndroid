package me.arnaumas.appgeorgina.objectes

import java.util.*

data class Message (
    var message: String = "",
    var from: String = "",
    var dob: Date = Date() //Date Of Birth (quan s'ha creat el missatge)
)