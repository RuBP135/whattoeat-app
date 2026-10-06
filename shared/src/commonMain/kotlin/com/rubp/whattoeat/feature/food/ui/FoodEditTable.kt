package com.rubp.whattoeat.feature.food.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Add
import com.composables.icons.materialicons.filled.Delete
import com.rubp.whattoeat.core.components.LeftSwipeBox
import com.rubp.whattoeat.core.components.button.CircleIconButton
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import org.jetbrains.compose.resources.painterResource
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.filled_star
import whattoeat.shared.generated.resources.outlined_star
import kotlin.math.max

@Composable
fun ScrollableTableTitleRow(
    modifier: Modifier,
    selectedTableId: Long,
    tables: List<FoodTable>,
    onTableSelected: (Long) -> Unit,
    onAddTable: () -> Unit,
){
    val selectedIndex = remember(selectedTableId, tables) {
        max(0, tables.indexOfFirst{ it.id == selectedTableId }) // 空的时候只能选择Tab“添加表格”了
    }
    PrimaryScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex
    ) {
        tables.forEachIndexed { index, table ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTableSelected(table.id) },
                text = {
                    Text(
                        text = table.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }

        Tab(
            selected = false,
            onClick = onAddTable,
            text = {
                Text(
                    text = "添加表格",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            icon = {
                Icon(
                    imageVector = MaterialIcons.Filled.Add,
                    contentDescription = "添加表格"
                )
            }
        )
    }
}

@Composable
fun EditTable(
    modifier: Modifier,
    foods: List<Food>,
    onClickStar: (food: Food) -> Unit,
    onInputName: (food: Food, name: String) -> Unit,
    onInputWeight: (food: Food, weight: Int) -> Unit,
    onClickDelFood: (food: Food) -> Unit,
) {
    val weightList = listOf(2f, 8f, 3f)
    val titleColor = MaterialTheme.colorScheme.primary
    val titleStyle = MaterialTheme.typography.titleMedium


    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        stickyHeader {
            FoodTableRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 5.dp),
                cells = listOf(
                    Cell({ Text(text = "参选", color = titleColor, style = titleStyle) }, weightList[0]),
                    Cell({ Text(text = "名称", color = titleColor, style = titleStyle) }, weightList[1]),
                    Cell({ Text(text = "权重", color = titleColor, style = titleStyle) }, weightList[2])
                )
            )
        }

        items(foods, key = { it.id }) { food ->
            SwipeRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 2.dp)
                    .animateItem(),
                food = food,
                weightList = weightList,
                onClickStar = onClickStar,
                onInputName = onInputName,
                onInputWeight = onInputWeight,
                onDelete = { onClickDelFood(food) }
            )
        }
    }
}

@Composable
private fun SwipeRow(
    modifier: Modifier,
    food: Food,
    weightList: List<Float>,
    onClickStar: (food: Food) -> Unit,
    onInputName: (food: Food, name: String) -> Unit,
    onInputWeight: (food: Food, weight: Int) -> Unit,
    onDelete: () -> Unit
) {
    var isError by remember { mutableStateOf(false) }
    var foodName by remember(food.id) { mutableStateOf(TextFieldValue(food.name)) }
    var foodWeight by remember(food.id) { mutableStateOf(TextFieldValue(food.weight.toString())) }
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
    )

    LeftSwipeBox(
        menuWidthDp = 60.dp,
        modifier = modifier,
        menuContent = {
            CircleIconButton(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 3.dp),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = onDelete
            ) {
                Icon(
                    imageVector = (MaterialIcons.Filled.Delete),
                    contentDescription = "删除此行数据"
                )
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        ){
            FoodTableRow(
                modifier = Modifier.fillMaxSize(),
                cells = listOf(
                    Cell( // 参选Star
                        content = {
                            IconButton({ onClickStar(food) }) {
                                Image(
                                    painter = painterResource(
                                        if (food.marked) Res.drawable.filled_star
                                        else Res.drawable.outlined_star
                                    ),
                                    contentDescription = "参选"
                                )
                            }
                        },
                        weight = weightList[0]
                    ),
                    Cell( // 食物名称
                        content = {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                OutlinedTextField(
                                    value = foodName,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    colors = textFieldColors,
                                    placeholder = {
                                        Text(
                                            text = "请输入名称",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    onValueChange = { newFoodName ->
                                        foodName = newFoodName
                                        onInputName(food, newFoodName.text)
                                    }
                                )
                            }
                        },
                        weight = weightList[1]
                    ),
                    Cell( // 权重
                        content = {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                OutlinedTextField(
                                    value = foodWeight,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    colors = textFieldColors,
                                    onValueChange = { newFoodWeight ->
                                        foodWeight = newFoodWeight
                                        val text = newFoodWeight.text
                                        if (text.isNotEmpty() && text.length <= 5 && text.all { it.isDigit() }) {
                                            onInputWeight(food, text.toInt())
                                            isError = false
                                        } else {
                                            isError = text.isNotEmpty()
                                        }
                                    },
                                    isError = isError,
                                    supportingText = if (isError) {
                                        { Text("请输入有效数字", color = MaterialTheme.colorScheme.error) }
                                    } else null,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                            }
                        },
                        weight = weightList[2]
                    )
                )
            )
        }

    }
}