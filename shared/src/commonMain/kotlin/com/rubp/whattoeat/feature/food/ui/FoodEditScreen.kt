package com.rubp.whattoeat.feature.food.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.components.WteSnackbar
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.domain.FoodTableDto
import com.rubp.whattoeat.feature.food.domain.foodTableToJson
import com.rubp.whattoeat.feature.food.domain.jsonToFoodTableDto
import com.rubp.whattoeat.core.components.WteTopBar
import com.rubp.whattoeat.core.components.button.MenuButton
import com.rubp.whattoeat.core.components.button.WtePrimaryButton
import com.rubp.whattoeat.core.components.button.WteSecondaryButton
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException

@Composable
fun FoodEditScreen(
    foodViewModel: FoodViewModel,
    modifier: Modifier = Modifier,
    onReturnToEat: () -> Unit
) {
    val foods by foodViewModel.foods.collectAsState(initial = emptyList())
    val tables by foodViewModel.tables.collectAsState()
    val currentTable by foodViewModel.currentTable.collectAsState()

    FoodEditContent(
        modifier = modifier,
        foods = foods,
        tables = tables,
        currentTable = currentTable,
        onReturnToEat = onReturnToEat,
        onTableSelected = foodViewModel::switchTable,
        onRenameTable = foodViewModel::renameTable,
        onDeleteTable = foodViewModel::deleteTable,
        onCreateTable = foodViewModel::createTable,
        onImportFoodAndTable = foodViewModel::inputFoodTableDto,
        onAddFood = { foodViewModel.insert(Food(name = "", weight = 1, marked = true)) },
        onDeleteFood = foodViewModel::delete,
        onClickStar = { foodViewModel.update(it.copy(marked = !it.marked)) },
        onInputName = { food, name -> foodViewModel.update(food.copy(name = name)) },
        onInputWeight = { food, weight -> foodViewModel.update(food.copy(weight = weight)) }
    )
}

@Composable
private fun FoodEditContent(
    modifier: Modifier = Modifier,
    foods: List<Food>,
    tables: List<FoodTable>,
    currentTable: FoodTable?,
    onReturnToEat: () -> Unit,
    onTableSelected: (Long) -> Unit,
    onRenameTable: (Long, String) -> Unit,
    onDeleteTable: (Long) -> Unit,
    onCreateTable: (String) -> Unit,
    onImportFoodAndTable: (FoodTableDto) -> Unit,
    onAddFood: () -> Unit,
    onDeleteFood: (Food) -> Unit,
    onClickStar: (Food) -> Unit,
    onInputName: (Food, String) -> Unit,
    onInputWeight: (Food, Int) -> Unit
) {
    var editDialogState: EditDialogState by remember { mutableStateOf(EditDialogState.None) }
    val tableName = currentTable?.name ?: ""

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() // 获取与当前UI组件生命周期绑定的协程作用域
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = modifier,
        topBar = {
            WteTopBar(onReturnToEat, "编辑清单"){ closeMenu ->
                MenuButton("新建表格"){ closeMenu(); editDialogState = EditDialogState.CreateTable }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("重命名表格"){ closeMenu(); editDialogState = EditDialogState.RenameTable }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("删除表格"){ closeMenu(); editDialogState = EditDialogState.DeleteTable }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("导入表格"){
                    closeMenu()
                    scope.launch {
                        try {
                            val input = clipboardManager.getText()?.text
                            if(input == null){
                                snackbarHostState.showSnackbar("未获取到表格数据")
                                return@launch
                            }
                            val foodTableDto = jsonToFoodTableDto(input)
                            onImportFoodAndTable(foodTableDto)
                            snackbarHostState.showSnackbar("已导入表格")
                        } catch(e: SerializationException){
                            e.printStackTrace()
                            snackbarHostState.showSnackbar("json格式错误")
                        }
                    }
                }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("导出表格"){
                    closeMenu()
                    scope.launch {
                        if(currentTable == null){
                            snackbarHostState.showSnackbar("当前没有选中表格")
                        } else {
                            clipboardManager.setText(AnnotatedString(foodTableToJson(currentTable, foods)))
                            snackbarHostState.showSnackbar("已导出json至剪贴板")
                        }
                    }
                }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("帮助"){ closeMenu(); editDialogState = EditDialogState.Help }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { WteSnackbar(it) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            // 表格标题
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = tableName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 滚动标题栏（切换表格）
            ScrollableTableTitleRow(
                modifier = Modifier
                    .fillMaxWidth(),
                selectedTableId = currentTable?.id ?: -1L,
                tables = tables,
                onTableSelected = onTableSelected,
                onAddTable = { editDialogState = EditDialogState.CreateTable } // 拉出创建对话框
            )

            // 编辑表
            FoodEditTable(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                foods = foods,
                onClickStar = onClickStar,
                onInputName = onInputName,
                onInputWeight = onInputWeight,
                onClickDelFood = { editDialogState = EditDialogState.DeleteFood(it) },
            )

            // 底部按钮
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ){
                WtePrimaryButton(
                    text = "添加新菜品",
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp)
                ) { onAddFood() }
                WteSecondaryButton(
                    text = "保存",
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {}
            }
        }
    }

    FoodEditDialogHandler(
        dialogState = editDialogState,
        tableName = tableName,
        currentTable = currentTable,
        onCreateTable = onCreateTable,
        onRenameTable = onRenameTable,
        onDeleteTable = onDeleteTable,
        onDeleteFood = onDeleteFood,
        onDismiss = { editDialogState = EditDialogState.None }
    )
}

@Preview
@Composable
private fun FoodEditContentPreview() {
    WhatToEatPreviewTheme {
        FoodEditContent(
            foods = emptyList(),
            tables = listOf(
                FoodTable(1L, "早餐", 0L),
                FoodTable(2L, "午餐", 1L)
            ),
            currentTable = FoodTable(1L, "早餐", 0L),
            onReturnToEat = {},
            onTableSelected = {},
            onRenameTable = { _, _ -> },
            onDeleteTable = {},
            onCreateTable = {},
            onImportFoodAndTable = {},
            onAddFood = {},
            onDeleteFood = {},
            onClickStar = {},
            onInputName = { _, _ -> },
            onInputWeight = { _, _ -> }
        )
    }
}
