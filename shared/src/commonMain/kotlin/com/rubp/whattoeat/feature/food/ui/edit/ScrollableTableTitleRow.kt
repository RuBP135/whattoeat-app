package com.rubp.whattoeat.feature.food.ui.edit

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Add
import com.rubp.whattoeat.feature.food.data.entity.FoodTable

@Composable
fun ScrollableTableTitleRow(
    modifier: Modifier = Modifier,
    selectedTableId: Long?,
    tables: List<FoodTable>,
    onTableSelected: (Long) -> Unit,
    onAddTable: () -> Unit
) {
    val selectedIndex = tables.indexOfFirst { it.id == selectedTableId }

    PrimaryScrollableTabRow(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        selectedTabIndex = selectedIndex.coerceAtLeast(0),
        indicator = {
            if(selectedIndex >= 0) {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedIndex, matchContentSize = true),
                    width = Dp.Unspecified,
                )
            }
        }
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
            selected = false,
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
