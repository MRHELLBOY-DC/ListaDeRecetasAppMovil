package com.example.recipeplannerlite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeplannerlite.data.model.Recipe
import com.example.recipeplannerlite.data.model.ShoppingItem
import com.example.recipeplannerlite.data.model.WeekDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DayPlanItem(
    val day: WeekDay,
    val recipe: Recipe? = null,
    val showRecipeSelector: Boolean = false
)

class WeekPlannerViewModel : ViewModel() {

    private val _dayPlans = MutableStateFlow(
        WeekDay.entries.map { day ->
            DayPlanItem(day = day)
        }
    )

    val dayPlans: StateFlow<List<DayPlanItem>> = _dayPlans.asStateFlow()

    private val _checkedIngredients = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    val shoppingItems: StateFlow<List<ShoppingItem>> = combine(
        _dayPlans,
        _checkedIngredients
    ) { dayPlans, checkedIngredients ->

        val groupedIngredients = dayPlans
            .mapNotNull { it.recipe }
            .flatMap { it.ingredients }
            .groupBy { ingredient ->
                normalizeIngredientKey(ingredient.name)
            }

        groupedIngredients
            .map { (normalizedName, ingredients) ->
                val displayName = ingredients.first().name.trim()
                val mergedQuantity = mergeQuantities(
                    ingredients.map { it.quantity }
                )

                ShoppingItem(
                    name = displayName,
                    quantity = mergedQuantity,
                    checked = checkedIngredients[normalizedName] ?: false
                )
            }
            .sortedBy { it.name.lowercase() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun showRecipeSelector(day: WeekDay) {
        _dayPlans.value = _dayPlans.value.map { item ->
            if (item.day == day) {
                item.copy(showRecipeSelector = true)
            } else {
                item.copy(showRecipeSelector = false)
            }
        }
    }

    fun hideRecipeSelector(day: WeekDay) {
        _dayPlans.value = _dayPlans.value.map { item ->
            if (item.day == day) {
                item.copy(showRecipeSelector = false)
            } else {
                item
            }
        }
    }

    fun hideAllRecipeSelectors() {
        _dayPlans.value = _dayPlans.value.map { item ->
            item.copy(showRecipeSelector = false)
        }
    }

    fun assignRecipe(day: WeekDay, recipe: Recipe) {
        _dayPlans.value = _dayPlans.value.map { item ->
            if (item.day == day) {
                item.copy(
                    recipe = recipe.copy(day = day),
                    showRecipeSelector = false
                )
            } else {
                item
            }
        }
    }

    fun deleteRecipe(day: WeekDay) {
        _dayPlans.value = _dayPlans.value.map { item ->
            if (item.day == day) {
                item.copy(
                    recipe = null,
                    showRecipeSelector = false
                )
            } else {
                item
            }
        }
    }

    fun getDayPlan(day: WeekDay): DayPlanItem? {
        return _dayPlans.value.find { item ->
            item.day == day
        }
    }

    fun resetWeekPlan() {
        _dayPlans.value = WeekDay.entries.map { day ->
            DayPlanItem(day = day)
        }
        _checkedIngredients.value = emptyMap()
    }

    fun setShoppingItemChecked(name: String, checked: Boolean) {
        val key = normalizeIngredientKey(name)
        _checkedIngredients.value = _checkedIngredients.value.toMutableMap().apply {
            this[key] = checked
        }
    }

    fun toggleShoppingItemChecked(name: String) {
        val key = normalizeIngredientKey(name)
        val currentValue = _checkedIngredients.value[key] ?: false

        _checkedIngredients.value = _checkedIngredients.value.toMutableMap().apply {
            this[key] = !currentValue
        }
    }

    fun clearPurchasedMarks() {
        _checkedIngredients.value = emptyMap()
    }

    private fun normalizeIngredientKey(name: String): String {
        return name.trim().lowercase()
    }

    private fun mergeQuantities(quantities: List<String>): String {
        val cleanedQuantities = quantities
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val grouped = cleanedQuantities.groupingBy { it }.eachCount()

        return grouped.entries.joinToString(" + ") { entry ->
            if (entry.value == 1) {
                entry.key
            } else {
                "${entry.value} x ${entry.key}"
            }
        }
    }
}