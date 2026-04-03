package com.example.recipeplannerlite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import com.example.recipeplannerlite.ui.screens.CreateRecipeScreen
import com.example.recipeplannerlite.ui.screens.RecipeListScreen
import com.example.recipeplannerlite.ui.theme.RecipePlannerLiteTheme
import com.example.recipeplannerlite.viewmodel.CreateRecipeViewModel
import com.example.recipeplannerlite.viewmodel.RecipePlannerViewModel

class MainActivity : ComponentActivity() {
    private val recipePlannerViewModel: RecipePlannerViewModel by viewModels()
    private val createRecipeViewModel: CreateRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val filteredRecipes by recipePlannerViewModel.filteredRecipes.collectAsState()
            val searchQuery by recipePlannerViewModel.searchQuery.collectAsState()
            val ingredientFilter by recipePlannerViewModel.ingredientFilter.collectAsState()
            val draftTitle by createRecipeViewModel.draftTitle.collectAsState()
            val draftIngredients by createRecipeViewModel.draftIngredients.collectAsState()
            var isCreateRecipeView by remember { mutableStateOf(false) }

            RecipePlannerLiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isCreateRecipeView) {
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
                                if (saved) isCreateRecipeView = false
                                saved
                            },
                            onCancel = {
                                createRecipeViewModel.resetDraft()
                                isCreateRecipeView = false
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        RecipeListScreen(
                            recipes = filteredRecipes,
                            searchQuery = searchQuery,
                            ingredientFilter = ingredientFilter,
                            onSearchQueryChange = recipePlannerViewModel::updateSearchQuery,
                            onIngredientFilterChange = recipePlannerViewModel::updateIngredientFilter,
                            onCreateRecipeClick = { isCreateRecipeView = true },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
