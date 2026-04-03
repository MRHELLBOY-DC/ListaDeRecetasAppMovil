package com.example.recipeplannerlite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeplannerlite.data.model.Recipe
import com.example.recipeplannerlite.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class RecipePlannerViewModel(
    private val repository: RecipeRepository = RecipeRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _ingredientFilter = MutableStateFlow("")
    val ingredientFilter: StateFlow<String> = _ingredientFilter.asStateFlow()

    val filteredRecipes: StateFlow<List<Recipe>> = combine(
        repository.recipes,
        _searchQuery,
        _ingredientFilter
    ) { recipes, query, ingredient ->
        applyFilters(recipes, query, ingredient)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateIngredientFilter(filter: String) {
        _ingredientFilter.value = filter
    }


    private fun applyFilters(recipes: List<Recipe>, query: String, ingredient: String): List<Recipe> {
        val cleanedQuery = query.trim()
        val cleanedIngredient = ingredient.trim()

        return recipes.filter { recipe ->
            val matchesTitle = recipe.title.contains(cleanedQuery, ignoreCase = true)
            val matchesIngredient = cleanedIngredient.isBlank() || recipe.ingredients.any { item ->
                item.name.contains(cleanedIngredient, ignoreCase = true)
            }
            matchesTitle && matchesIngredient
        }
    }

}

