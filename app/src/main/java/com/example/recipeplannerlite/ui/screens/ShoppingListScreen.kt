package com.example.recipeplannerlite.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.recipeplannerlite.data.model.ShoppingItem

@Composable
fun ShoppingListScreen(
    items: List<ShoppingItem>,
    onItemCheckedChange: (String, Boolean) -> Unit,
    onBackClick: () -> Unit,
    onClearPurchasedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val purchasedCount = items.count { it.checked }
    val pendingCount = items.size - purchasedCount

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Lista de compras",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Pendientes: $pendingCount | Comprados: $purchasedCount",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver al plan")
            }

            TextButton(
                onClick = onClearPurchasedClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpiar marcas")
            }
        }

        if (items.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Todavía no hay ingredientes en la lista.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Asigna recetas al plan semanal y esta pantalla se generará automáticamente.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = items,
                    key = { item -> item.name.lowercase() }
                ) { item ->
                    ShoppingItemCard(
                        item = item,
                        onCheckedChange = { checked ->
                            onItemCheckedChange(item.name, checked)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!item.checked) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = item.checked,
                onCheckedChange = onCheckedChange
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (item.checked) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                Text(
                    text = item.quantity,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = if (item.checked) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
            }
        }
    }
}