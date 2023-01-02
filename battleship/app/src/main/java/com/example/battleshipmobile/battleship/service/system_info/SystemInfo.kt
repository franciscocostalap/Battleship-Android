package com.example.battleshipmobile.battleship.service.system_info

data class SystemInfo(
    val authors : List<Author>,
    val version : String
    ){

    data class Author(
        val name : String,
        val email : String,
        val github : String,
        val iselID : Int
    )
}