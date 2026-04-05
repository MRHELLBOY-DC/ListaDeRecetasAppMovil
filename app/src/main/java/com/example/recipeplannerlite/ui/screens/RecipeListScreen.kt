package com.example.recipeplannerlite.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeplannerlite.data.model.Recipe

@Composable
fun RecipeListScreen(
    recipes: List<Recipe>,
    searchQuery: String,
    ingredientFilter: String,
    onSearchQueryChange: (String) -> Unit,
    onIngredientFilterChange: (String) -> Unit,
    onCreateRecipeClick: () -> Unit,
    onWeekPlannerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val noBlueFocusColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.outline,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.onSurface
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Recipe Planner Lite", style = MaterialTheme.typography.headlineSmall)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                onCreateRecipeClick()
            }) {
                Text("Crear receta")
            }
            Button(onClick = onWeekPlannerClick){
                Text("Ver listado semanal")
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Buscar por nombre") },
            colors = noBlueFocusColors,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ingredientFilter,
            onValueChange = onIngredientFilterChange,
            label = { Text("Filtrar por ingrediente") },
            colors = noBlueFocusColors,
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Recetas (${recipes.size})", style = MaterialTheme.typography.titleMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(recipes) { recipe ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
                        recipe.ingredients.forEach { ingredient ->
                            Text(text = "- ${ingredient.name}: ${ingredient.quantity}")
                        }
                    }
                }
            }
        }
    }
}
