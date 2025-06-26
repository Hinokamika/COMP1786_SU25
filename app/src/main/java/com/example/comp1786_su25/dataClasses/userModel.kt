package com.example.comp1786_su25.dataClasses

data class userModel(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val age: String = "",
    val classList: List<String> = emptyList(),
)
