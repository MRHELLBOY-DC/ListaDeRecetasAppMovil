package com.example.recipeplannerlite.data.model

data class Recipe(
    val title: String,
    val ingredients: List<Ingredient>,
    val instructions: String,
    val day: WeekDay? = null
)

