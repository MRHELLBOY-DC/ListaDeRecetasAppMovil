package com.example.recipeplannerlite.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.HorizontalDivider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card


import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeplannerlite.data.model.Ingredient
import com.example.recipeplannerlite.data.model.Recipe
import com.example.recipeplannerlite.data.model.WeekDay
import com.example.recipeplannerlite.ui.theme.RecipePlannerLiteTheme
import com.example.recipeplannerlite.viewmodel.DayPlanItem

@Composable
fun WeekPlannerScreen(
    dayPlans: List<DayPlanItem>,
    availableRecipes: List<Recipe>,
    onShowSelector: (WeekDay) -> Unit,
    onHideSelector: (WeekDay) -> Unit,
    onAssignRecipe: (WeekDay, Recipe) -> Unit,
    onDeleteAssignation: (WeekDay) -> Unit,
    onShoppingListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Button(
                onClick = onShoppingListClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Ver lista de compras")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeekPlannerHeader()

            WeekPlannerList(
                dayPlans = dayPlans,
                availableRecipes = availableRecipes,
                onShowSelector = onShowSelector,
                onHideSelector = onHideSelector,
                onAssignRecipe = onAssignRecipe,
                onDeleteAssignation = onDeleteAssignation,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun WeekPlannerHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Plan semanal",
        modifier = modifier.fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun WeekPlannerList(
    dayPlans: List<DayPlanItem>,
    availableRecipes: List<Recipe>,
    onShowSelector: (WeekDay) -> Unit,
    onHideSelector: (WeekDay) -> Unit,
    onAssignRecipe: (WeekDay, Recipe) -> Unit,
    onDeleteAssignation: (WeekDay) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = dayPlans,
            key = { item -> item.day.name }
        ) { item ->
            WeekPlannerItem(
                item = item,
                availableRecipes = availableRecipes,
                onShowSelector = onShowSelector,
                onHideSelector = onHideSelector,
                onAssignRecipe = onAssignRecipe,
                onDeleteAssignation = onDeleteAssignation
            )
        }
    }
}

@Composable
fun WeekPlannerItem(
    item: DayPlanItem,
    availableRecipes: List<Recipe>,
    onShowSelector: (WeekDay) -> Unit,
    onHideSelector: (WeekDay) -> Unit,
    onAssignRecipe: (WeekDay, Recipe) -> Unit,
    onDeleteAssignation: (WeekDay) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardHeader(
                day = item.day,
                onEditClick = {
                    if (item.showRecipeSelector) {
                        onHideSelector(item.day)
                    } else {
                        onShowSelector(item.day)
                    }
                },
                onDeleteClick = {
                    onDeleteAssignation(item.day)
                }
            )

            if (item.showRecipeSelector) {
                RecipeDropdown(
                    expanded = true,
                    selectedRecipe = item.recipe,
                    availableRecipes = availableRecipes,
                    onDismissRequest = { onHideSelector(item.day) },
                    onRecipeSelected = { recipe ->
                        onAssignRecipe(item.day, recipe)
                        onHideSelector(item.day)
                    }
                )
            } else {
                RecipeLabel(recipe = item.recipe)
            }
        }
    }
}
@Composable
fun CardHeader(
    day: WeekDay,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DayLabel(day = day)

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                UpdateButton(onClick = onEditClick)
                DeleteButton(onClick = onDeleteClick)
            }
        }
    }
}

@Composable
fun UpdateButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = "Modificar receta",
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun DeleteButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar asignación",
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun DayLabel(
    day: WeekDay,
    modifier: Modifier = Modifier
) {
    Text(
        text = getDayLabel(day),
        modifier = modifier,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun RecipeLabel(
    recipe: Recipe?,
    modifier: Modifier = Modifier
) {
    Text(
        text = recipe?.title ?: "Sin receta asignada",
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFF3F3),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
}

@Composable
fun RecipeDropdown(
    expanded: Boolean,
    selectedRecipe: Recipe?,
    availableRecipes: List<Recipe>,
    onDismissRequest: () -> Unit,
    onRecipeSelected: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        OutlinedTextField(
            value = selectedRecipe?.title ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccionar receta") },
            modifier = Modifier.fillMaxWidth()
        )

        if (expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    availableRecipes.forEachIndexed { index, recipe ->
                        Text(
                            text = recipe.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onRecipeSelected(recipe)
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        )

                        if (index < availableRecipes.lastIndex) {
                            HorizontalDivider()
                        }
                    }

                    HorizontalDivider()

                    Text(
                        text = "Cerrar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDismissRequest() }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

fun getDayLabel(day: WeekDay): String {
    return when (day) {
        WeekDay.MONDAY -> "Lunes"
        WeekDay.TUESDAY -> "Martes"
        WeekDay.WEDNESDAY -> "Miércoles"
        WeekDay.THURSDAY -> "Jueves"
        WeekDay.FRIDAY -> "Viernes"
        WeekDay.SATURDAY -> "Sábado"
        WeekDay.SUNDAY -> "Domingo"
    }
}

@Preview(showBackground = true)
@Composable
fun WeekPlannerScreenPreview() {
    val availableRecipes = listOf(
        Recipe(
            title = "Pizza",
            ingredients = listOf(Ingredient("Queso", "200 g")),
            instructions = "Hornear"
        ),
        Recipe(
            title = "Pasta",
            ingredients = listOf(Ingredient("Fideos", "300 g")),
            instructions = "Cocer"
        ),
        Recipe(
            title = "Ensalada",
            ingredients = listOf(Ingredient("Lechuga", "1 unidad")),
            instructions = "Mezclar"
        )
    )

    val dayPlans = listOf(
        DayPlanItem(WeekDay.MONDAY, availableRecipes[0], false),
        DayPlanItem(WeekDay.TUESDAY, null, false),
        DayPlanItem(WeekDay.WEDNESDAY, availableRecipes[1], true),
        DayPlanItem(WeekDay.THURSDAY, null, false),
        DayPlanItem(WeekDay.FRIDAY, null, false),
        DayPlanItem(WeekDay.SATURDAY, availableRecipes[2], false),
        DayPlanItem(WeekDay.SUNDAY, null, false)
    )

    RecipePlannerLiteTheme {
        WeekPlannerScreen(
            dayPlans = dayPlans,
            availableRecipes = availableRecipes,
            onShowSelector = {},
            onHideSelector = {},
            onAssignRecipe = { _, _ -> },
            onDeleteAssignation = {},
            onShoppingListClick = {}
        )
    }
}