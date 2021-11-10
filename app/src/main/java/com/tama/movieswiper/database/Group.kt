package com.tama.movieswiper.database

data class Group(
    val name: String = "",
    val owner: String = "",
    val users: List<User> = emptyList()
)