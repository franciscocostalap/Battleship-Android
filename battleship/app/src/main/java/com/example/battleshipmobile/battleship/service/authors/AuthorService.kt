package com.example.battleshipmobile.battleship.service.authors


interface AuthorService {

    /**
     * 
     */
    suspend fun getAuthors(): List<Author>

}