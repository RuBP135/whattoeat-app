package com.rubp.whattoeat.feature.food.ui.choose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Block
import com.composables.icons.materialicons.filled.Clear
import com.composables.icons.materialicons.filled.Clear_all
import com.composables.icons.materialicons.filled.Edit
import com.rubp.whattoeat.core.ui.components.WteTopBar
import com.rubp.whattoeat.core.ui.components.button.CardButton
import com.rubp.whattoeat.core.ui.components.button.CircleIconButton
import com.rubp.whattoeat.core.ui.components.button.WtePrimaryButton
import com.rubp.whattoeat.core.ui.components.card.WtePaperCard
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.model.FoodDataState
import com.rubp.whattoeat.feature.food.model.FoodSelectionState
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel

@Composable
fun FoodChooseScreen(
    foodViewModel: FoodViewModel,
    onNavigateToFoodEdit: () -> Unit,
    onReturnToHome: () -> Unit
) {
    val state by foodViewModel.foodDataStateFlow.collectAsState()
    val selected = state as? FoodDataState.Selected
    val tables = when (val state = state) {
        is FoodDataState.Selected -> state.tables
        is FoodDataState.NoTableSelected -> state.tables
        else -> emptyList()
    }
    val currentTable = selected?.currentTable
    val resultMessage: String = when (val state = state) {
        FoodDataState.Loading -> "正在读取清单…"
        is FoodDataState.Error -> "清单加载失败，请稍后重试"
        is FoodDataState.NoTableSelected -> "请先选择一个表格"
        is FoodDataState.Selected -> when (val selectionState = state.selectionState) {
            is FoodSelectionState.Chosen -> selectionState.food.name
            FoodSelectionState.NoAvailableFood -> "没有可供选择的食物！"
            FoodSelectionState.Idle -> "点击查询今天吃什么"
        }
    }

    FoodChooseContent(
        resultMessage = resultMessage,
        tables = tables,
        currentTable = currentTable,
        onNavigateToFoodEdit = onNavigateToFoodEdit,
        onReturnToHome = onReturnToHome,
        onTableSelected = { foodViewModel.switchTable(it) },
        onClickRandomFood = { foodViewModel.chosenRandomFood() },
        onClickClear = foodViewModel::clearSelection,
        onClickIgnore = foodViewModel::ignoreChosenFood,
        onClickClearIgnore = foodViewModel::recoverAll
    )
}

@Composable
private fun FoodChooseContent(
    resultMessage: String,
    tables: List<FoodTable>,
    currentTable: FoodTable?,
    onNavigateToFoodEdit: () -> Unit,
    onReturnToHome: () -> Unit,
    onTableSelected: (Long) -> Unit,
    onClickRandomFood: () -> Unit,
    onClickClear: () -> Unit,
    onClickIgnore: () -> Unit,
    onClickClearIgnore: () -> Unit
) {
    Scaffold(
        topBar = { WteTopBar(onReturnToHome, "选择食物") },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(top = 70.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                WtePaperCard(
                    modifier = Modifier
                        .width(250.dp)
                        .heightIn(min = 70.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = resultMessage,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val width = 150.dp
                    val height = 55.dp
                    val modifier = Modifier
                        .width(width)
                        .height(height)

                    WtePrimaryButton(
                        text = "查询",
                        modifier = modifier,
                        onClick = onClickRandomFood
                    )
                    CardButton(
                        text = "清除",
                        modifier = modifier,
                        icon = {
                            Icon(
                                imageVector = MaterialIcons.Filled.Clear,
                                contentDescription = "清除"
                            )
                        }
                    ) { onClickClear() }
                    CardButton(
                        text = "忽略",
                        modifier = modifier,
                        icon = {
                            Icon(
                                imageVector = MaterialIcons.Filled.Block,
                                contentDescription = "忽略"
                            )
                        }
                    ) { onClickIgnore() }
                    CardButton(
                        text = "恢复全部",
                        modifier = modifier,
                        icon = {
                            Icon(
                                imageVector = MaterialIcons.Filled.Clear_all,
                                contentDescription = "恢复全部"
                            )
                        }
                    ) { onClickClearIgnore() }
                }
            }

            // 书签侧栏（右侧）
            BookmarkSidebar(
                tables = tables,
                currentTable = currentTable,
                onTableSelected = onTableSelected,
                modifier = Modifier.align(Alignment.CenterEnd)
            )

            // 编辑按钮
            CircleIconButton(
                onClick = onNavigateToFoodEdit,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = MaterialIcons.Filled.Edit,
                    contentDescription = "编辑"
                )
            }
        }
    }
}

@Preview
@Composable
private fun FoodChooseContentPreview() {
    WhatToEatPreviewTheme {
        FoodChooseContent(
            resultMessage = "显示一个食物名称",
            tables = listOf(
                FoodTable(1L, "默认", 0),
                FoodTable(2L, "午餐", 1),
                FoodTable(3L, "晚餐", 2)
            ),
            currentTable = FoodTable(1L, "默认", 0),
            onNavigateToFoodEdit = {},
            onReturnToHome = {},
            onTableSelected = {},
            onClickRandomFood = {},
            onClickClear = {},
            onClickIgnore = {},
            onClickClearIgnore = {}
        )
    }
}
