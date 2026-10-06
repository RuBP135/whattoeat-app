package com.rubp.whattoeat.feature.food.ui

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.rubp.whattoeat.core.components.button.CardButton
import com.rubp.whattoeat.core.components.button.CircleIconButton
import com.rubp.whattoeat.core.components.button.WtePrimaryButton
import com.rubp.whattoeat.core.components.card.WtePaperCard
import com.rubp.whattoeat.core.components.WteSnackbar
import com.rubp.whattoeat.core.components.WteTopBar
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import kotlinx.coroutines.launch

@Composable
fun EatScreen(
    foodViewModel: FoodViewModel,
    onNavigateToFoodEdit: () -> Unit,
    onReturnToHome: () -> Unit
) {
    val tables by foodViewModel.tables.collectAsState()
    val currentTable by foodViewModel.currentTable.collectAsState()
    var foodName by remember { mutableStateOf("点击查询今天吃什么") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    EatContent(
        foodName = foodName,
        tables = tables,
        currentTable = currentTable,
        snackbarHostState = snackbarHostState,
        onNavigateToFoodEdit = onNavigateToFoodEdit,
        onReturnToHome = onReturnToHome,
        onTableSelected = { tableId ->
            foodViewModel.switchTable(tableId)
            foodName = "点击查询今天吃什么"
        },
        onClickRandomFood = { foodName = foodViewModel.chosenRandomFood() },
        onClickClear = {
            foodName = "点击查询今天吃什么"
            scope.launch {
                snackbarHostState.showSnackbar("已清除当前选择")
            }
        },
        onClickIgnore = {
            foodViewModel.ignoreChosenFood()
            scope.launch {
                snackbarHostState.showSnackbar("已忽略当前选择食物")
            }
        },
        onClickClearIgnore = {
            foodViewModel.clearAllIgnore()
            scope.launch {
                snackbarHostState.showSnackbar("已恢复所有被忽略的食物")
            }
        }
    )
}

@Composable
private fun EatContent(
    foodName: String,
    tables: List<FoodTable>,
    currentTable: FoodTable?,
    snackbarHostState: SnackbarHostState,
    onNavigateToFoodEdit: () -> Unit,
    onReturnToHome: () -> Unit,
    onTableSelected: (Long) -> Unit,
    onClickRandomFood: () -> Unit,
    onClickClear: () -> Unit,
    onClickIgnore: () -> Unit,
    onClickClearIgnore: () -> Unit
) {
    Scaffold(
        topBar = { WteTopBar(onReturnToHome, "Eat") },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { WteSnackbar(it) }
            )
        }
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
                        text = foodName,
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
                        textColor = MaterialTheme.colorScheme.onPrimary,
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
private fun EatContentPreview() {
    WhatToEatPreviewTheme {
        EatContent(
            foodName = "显示一个食物名称",
            tables = listOf(
                FoodTable(1L, "默认", 0),
                FoodTable(2L, "午餐", 1),
                FoodTable(3L, "晚餐", 2)
            ),
            currentTable = FoodTable(1L, "默认", 0),
            snackbarHostState = remember { SnackbarHostState() },
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
