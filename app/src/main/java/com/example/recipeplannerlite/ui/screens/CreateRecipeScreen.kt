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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeplannerlite.viewmodel.CreateRecipeViewModel

@Composable
fun CreateRecipeScreen(
    titleInput: String,
    ingredients: List<CreateRecipeViewModel.IngredientDraft>,
    onTitleChange: (String) -> Unit,
    onIngredientNameChange: (Long, String) -> Unit,
    onIngredientQuantityChange: (Long, String) -> Unit,
    onAddIngredient: () -> Unit,
    onRemoveIngredient: (Long) -> Unit,
    onSaveRecipe: () -> Boolean,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Crear receta", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = titleInput,
            onValueChange = onTitleChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Ingredientes", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.weight(1f, fill = true),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ingredients, key = { it.id }) { ingredient ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ingredient.name,
                            onValueChange = { onIngredientNameChange(ingredient.id, it) },
                            label = { Text("Ingrediente") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = ingredient.quantity,
                            onValueChange = { onIngredientQuantityChange(ingredient.id, it) },
                            label = { Text("Cantidad") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { onRemoveIngredient(ingredient.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Eliminar ingrediente")
                        }
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onAddIngredient, modifier = Modifier.weight(1f)) {
                Text("+ Ingrediente")
            }
            Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Cancelar")
            }
        }

        Button(
            onClick = { onSaveRecipe() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar receta")
        }
    }
}

