package com.rubp.whattoeat.feature.food.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** 表头与编辑行共用三列布局，统一内边距、列间距和 2:8:3 的列宽比例。 */
@Composable
internal fun FoodTableRow(
    modifier: Modifier = Modifier,
    selection: @Composable () -> Unit,
    name: @Composable () -> Unit,
    weight: @Composable () -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(Modifier.weight(2f), contentAlignment = Alignment.Center) {
            selection()
        }
        Box(Modifier.weight(8f), contentAlignment = Alignment.Center) {
            name()
        }
        Box(Modifier.weight(3f), contentAlignment = Alignment.Center) {
            weight()
        }
    }
}
