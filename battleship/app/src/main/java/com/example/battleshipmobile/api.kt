package com.example.battleshipmobile

interface Api{
    fun login(login: String, password: String) : Int?
    fun register(text: String, text1: String): Boolean
}

class FakeApi : Api {
    override fun login(login: String, password: String): Int? {
        return 1
    }

    override fun register(text: String, text1: String) : Boolean{
        return true
    }


}