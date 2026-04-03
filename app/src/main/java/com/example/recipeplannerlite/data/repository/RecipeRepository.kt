package com.example.recipeplannerlite.data.repository

import com.example.recipeplannerlite.data.model.Ingredient
import com.example.recipeplannerlite.data.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Shared in-memory repository used by multiple ViewModels.
 */
object RecipeRepository {
    private val _recipes = MutableStateFlow(
        listOf(
            Recipe(
                title = "Pasta con tomate",
                ingredients = listOf(
                    Ingredient(name = "Pasta", quantity = "200 g"),
                    Ingredient(name = "Tomate", quantity = "2 unidades")
                ),
                instructions = "Hervir pasta y mezclar con tomate."
            ),
            Recipe(
                title = "Ensalada mixta",
                ingredients = listOf(
                    Ingredient(name = "Lechuga", quantity = "1 taza"),
                    Ingredient(name = "Tomate", quantity = "1 unidad")
                ),
                instructions = "Mezclar todos los ingredientes."
            )
        )
    )

    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    fun addRecipe(recipe: Recipe) {
        _recipes.value = _recipes.value + recipe
    }
}

