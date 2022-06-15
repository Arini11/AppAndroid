package me.arnaumas.appgeorgina.objectes

data class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
)