package com.example.recipeplannerlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.recipeplannerlite.data.repository.RecipeRepository
import com.example.recipeplannerlite.ui.screens.CreateRecipeScreen
import com.example.recipeplannerlite.ui.screens.RecipeListScreen
import com.example.recipeplannerlite.ui.screens.ShoppingListScreen
import com.example.recipeplannerlite.ui.screens.WeekPlannerScreen
import com.example.recipeplannerlite.ui.theme.RecipePlannerLiteTheme
import com.example.recipeplannerlite.viewmodel.CreateRecipeViewModel
import com.example.recipeplannerlite.viewmodel.RecipePlannerViewModel
import com.example.recipeplannerlite.viewmodel.WeekPlannerViewModel

class MainActivity : ComponentActivity() {

    private val recipePlannerViewModel: RecipePlannerViewModel by viewModels()
    private val createRecipeViewModel: CreateRecipeViewModel by viewModels()
    private val weekPlannerViewModel: WeekPlannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val filteredRecipes by recipePlannerViewModel.filteredRecipes.collectAsState()
            val searchQuery by recipePlannerViewModel.searchQuery.collectAsState()
            val ingredientFilter by recipePlannerViewModel.ingredientFilter.collectAsState()

            val draftTitle by createRecipeViewModel.draftTitle.collectAsState()
            val draftIngredients by createRecipeViewModel.draftIngredients.collectAsState()

            val dayPlans by weekPlannerViewModel.dayPlans.collectAsState()
            val shoppingItems by weekPlannerViewModel.shoppingItems.collectAsState()

            val allRecipes by RecipeRepository.recipes.collectAsState()

            var currentScreen by remember { mutableStateOf(AppScreen.LIST) }

            RecipePlannerLiteTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    when (currentScreen) {
                        AppScreen.LIST -> {
                            RecipeListScreen(
                                recipes = filteredRecipes,
                                searchQuery = searchQuery,
                                ingredientFilter = ingredientFilter,
                                onSearchQueryChange = recipePlannerViewModel::updateSearchQuery,
                                onIngredientFilterChange = recipePlannerViewModel::updateIngredientFilter,
                                onCreateRecipeClick = {
                                    currentScreen = AppScreen.CREATE_RECIPE
                                },
                                onWeekPlannerClick = {
                                    currentScreen = AppScreen.WEEK_PLANNER
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AppScreen.CREATE_RECIPE -> {
                            CreateRecipeScreen(
                                titleInput = draftTitle,
                                ingredients = draftIngredients,
                                onTitleChange = createRecipeViewModel::updateDraftTitle,
                                onIngredientNameChange = createRecipeViewModel::updateDraftIngredientName,
                                onIngredientQuantityChange = createRecipeViewModel::updateDraftIngredientQuantity,
                                onAddIngredient = createRecipeViewModel::addDraftIngredient,
                                onRemoveIngredient = createRecipeViewModel::removeDraftIngredient,
                                onSaveRecipe = {
                                    val saved = createRecipeViewModel.submitDraftRecipe()
                                    if (saved) {
                                        currentScreen = AppScreen.LIST
                                    }
                                    saved
                                },
                                onCancel = {
                                    createRecipeViewModel.resetDraft()
                                    currentScreen = AppScreen.LIST
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AppScreen.WEEK_PLANNER -> {
                            WeekPlannerScreen(
                                dayPlans = dayPlans,
                                availableRecipes = allRecipes,
                                onShowSelector = weekPlannerViewModel::showRecipeSelector,
                                onHideSelector = weekPlannerViewModel::hideRecipeSelector,
                                onAssignRecipe = weekPlannerViewModel::assignRecipe,
                                onDeleteAssignation = weekPlannerViewModel::deleteRecipe,
                                onShoppingListClick = {
                                    currentScreen = AppScreen.SHOPPING_LIST
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        AppScreen.SHOPPING_LIST -> {
                            ShoppingListScreen(
                                items = shoppingItems,
                                onItemCheckedChange = weekPlannerViewModel::setShoppingItemChecked,
                                onBackClick = {
                                    currentScreen = AppScreen.WEEK_PLANNER
                                },
                                onClearPurchasedClick = weekPlannerViewModel::clearPurchasedMarks,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class AppScreen {
    LIST,
    CREATE_RECIPE,
    WEEK_PLANNER,
    SHOPPING_LIST
}