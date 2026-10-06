package com.rubp.whattoeat.feature.food.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Add
import com.rubp.whattoeat.feature.food.data.entity.FoodTable

@Composable
fun ScrollableTableTitleRow(
    modifier: Modifier = Modifier,
    selectedTableId: Long,
    tables: List<FoodTable>,
    onTableSelected: (Long) -> Unit,
    onAddTable: () -> Unit
) {
    val selectedIndex = tables.indexOfFirst { it.id == selectedTableId }.coerceAtLeast(0)
    PrimaryScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex
    ) {
        tables.forEachIndexed { index, table ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTableSelected(table.id) },
                text = {
                    Text(table.name, style = MaterialTheme.typography.titleMedium)
                }
            )
        }
        Tab(
            selected = tables.isEmpty(),
            onClick = onAddTable,
            text = {
                Text("添加表格", style = MaterialTheme.typography.titleSmall)
            },
            icon = {
                Icon(MaterialIcons.Filled.Add, contentDescription = "添加表格")
            }
        )
    }
}
