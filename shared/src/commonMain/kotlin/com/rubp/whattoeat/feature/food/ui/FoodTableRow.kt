package com.rubp.whattoeat.feature.food.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class Cell(
    val content: @Composable () -> Unit,
    val weight: Float
)


@Composable
fun FoodTableRow(
    modifier: Modifier = Modifier,
    cells :List<Cell>
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ){
        for(cell in cells) {
            Box(
                modifier = Modifier.weight(cell.weight),
                contentAlignment = Alignment.Center
            ){
                cell.content()
            }
        }
    }
}