package com.example.recipeplannerlite.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipeplannerlite.data.model.Ingredient
import com.example.recipeplannerlite.data.model.Recipe
import com.example.recipeplannerlite.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateRecipeViewModel(
    private val repository: RecipeRepository = RecipeRepository
) : ViewModel() {
    data class IngredientDraft(
        val id: Long,
        val name: String = "",
        val quantity: String = ""
    )

    private val _draftTitle = MutableStateFlow("")
    val draftTitle: StateFlow<String> = _draftTitle.asStateFlow()

    private val _draftIngredients = MutableStateFlow(listOf(IngredientDraft(id = 1L)))
    val draftIngredients: StateFlow<List<IngredientDraft>> = _draftIngredients.asStateFlow()

    private var nextIngredientId = 2L

    fun updateDraftTitle(value: String) {
        _draftTitle.value = value
    }

    fun addDraftIngredient() {
        _draftIngredients.value = _draftIngredients.value + IngredientDraft(id = nextIngredientId++)
    }

    fun removeDraftIngredient(id: Long) {
        val updated = _draftIngredients.value.filterNot { it.id == id }
        _draftIngredients.value = if (updated.isEmpty()) listOf(IngredientDraft(id = nextIngredientId++)) else updated
    }

    fun updateDraftIngredientName(id: Long, value: String) {
        _draftIngredients.value = _draftIngredients.value.map { item ->
            if (item.id == id) item.copy(name = value) else item
        }
    }

    fun updateDraftIngredientQuantity(id: Long, value: String) {
        _draftIngredients.value = _draftIngredients.value.map { item ->
            if (item.id == id) item.copy(quantity = value) else item
        }
    }

    fun submitDraftRecipe(): Boolean {
        val cleanedTitle = _draftTitle.value.trim()
        val parsedIngredients = _draftIngredients.value.mapNotNull { item ->
            val name = item.name.trim()
            val quantity = item.quantity.trim()
            if (name.isBlank() || quantity.isBlank()) null else Ingredient(name = name, quantity = quantity)
        }

        if (cleanedTitle.isBlank() || parsedIngredients.isEmpty()) {
            return false
        }

        repository.addRecipe(
            Recipe(
                title = cleanedTitle,
                ingredients = parsedIngredients,
                instructions = ""
            )
        )

        resetDraft()
        return true
    }

    fun resetDraft() {
        _draftTitle.value = ""
        _draftIngredients.value = listOf(IngredientDraft(id = nextIngredientId++))
    }
}

