package com.rubp.whattoeat.feature.food.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.feature.food.data.entity.Food

@Composable
fun FoodEditTable(
    modifier: Modifier = Modifier,
    foods: List<Food>,
    onClickStar: (Food) -> Unit,
    onInputName: (Food, String) -> Unit,
    onInputWeight: (Food, Int) -> Unit,
    onClickDelFood: (Food) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        stickyHeader {
            FoodTableHeader(modifier = Modifier.fillMaxWidth())
        }
        items(foods, key = { it.id }) { food ->
            FoodEditRow(
                modifier = Modifier.fillMaxWidth().animateItem(),
                food = food,
                onClickStar = { onClickStar(food) },
                onInputName = { onInputName(food, it) },
                onInputWeight = { onInputWeight(food, it) },
                onDelete = { onClickDelFood(food) }
            )
        }
    }
}

@Composable
private fun FoodTableHeader(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.primary
    val style = MaterialTheme.typography.titleMedium

    FoodTableRow(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .heightIn(min = 36.dp)
            .padding(vertical = 4.dp),
        selection = { Text("参选", color = color, style = style) },
        name = { Text("名称", color = color, style = style) },
        weight = { Text("权重", color = color, style = style) }
    )
}

@Preview
@Composable
private fun FoodEditTablePreview() {
    WhatToEatPreviewTheme {
        FoodEditTable(
            foods = listOf(
                Food(id = 1L, name = "糖醋排骨", weight = 1, marked = true),
                Food(id = 2L, name = "鱼香肉丝", weight = 2, marked = true),
                Food(id = 3L, name = "水煮肉片", weight = 3, marked = false)
            ),
            onClickStar = {},
            onInputName = { _, _ -> },
            onInputWeight = { _, _ -> },
            onClickDelFood = {}
        )
    }
}
