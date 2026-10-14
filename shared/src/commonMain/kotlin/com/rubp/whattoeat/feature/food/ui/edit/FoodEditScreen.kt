package com.rubp.whattoeat.feature.food.ui.edit

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.ui.components.WteTopBar
import com.rubp.whattoeat.core.ui.components.button.MenuButton
import com.rubp.whattoeat.core.ui.components.button.WtePrimaryButton
import com.rubp.whattoeat.core.ui.components.button.WteSecondaryButton
import com.rubp.whattoeat.core.ui.components.snackbar.WteSnackbar
import com.rubp.whattoeat.core.ui.components.snackbar.rememberWteSnackbarController
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.domain.exception.NoTableSelectedException
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import kotlinx.serialization.SerializationException

@Composable
fun FoodEditScreen(
    foodViewModel: FoodViewModel,
    modifier: Modifier = Modifier,
    onReturnToFoodChoose: () -> Unit
) {
    val foods by foodViewModel.foodListStateFlow.collectAsState(initial = emptyList())
    val tables by foodViewModel.tableListStateFlow.collectAsState()
    val currentTable by foodViewModel.currentTableStateFlow.collectAsState()

    FoodEditContent(
        modifier = modifier,
        foods = foods,
        tables = tables,
        currentTable = currentTable,
        onReturnToFoodChoose = onReturnToFoodChoose,
        onTableSelected = foodViewModel::switchTable,
        onRenameTable = foodViewModel::renameTable,
        onDeleteTable = foodViewModel::deleteTable,
        onCreateTable = foodViewModel::createTable,
        onImportTable = foodViewModel::importTable,
        onExportTable = foodViewModel::exportTable,
        onAddFood = { foodViewModel.insert(Food(name = "", weight = 1, marked = true)) },
        onDeleteFood = { foodViewModel.deleteById(it.id) },
        onClickStar = { foodViewModel.updateMarked(it.id, !it.marked) },
        onInputName = { food, name -> foodViewModel.updateName(food.id, name) },
        onInputWeight = { food, weight -> foodViewModel.updateWeight(food.id, weight) }
    )
}

@Composable
private fun FoodEditContent(
    modifier: Modifier = Modifier,
    foods: List<Food>,
    tables: List<FoodTable>,
    currentTable: FoodTable?,
    onReturnToFoodChoose: () -> Unit,
    onTableSelected: (Long) -> Unit,
    onRenameTable: (Long, String) -> Unit,
    onDeleteTable: (Long) -> Unit,
    onCreateTable: (String) -> Unit,
    onImportTable: (String) -> Unit,
    onExportTable: () -> String,
    onAddFood: () -> Unit,
    onDeleteFood: (Food) -> Unit,
    onClickStar: (Food) -> Unit,
    onInputName: (Food, String) -> Unit,
    onInputWeight: (Food, Int) -> Unit
) {
    var editDialogState: EditDialogState by remember { mutableStateOf(EditDialogState.None) }
    val tableName = currentTable?.name ?: ""

    val snackbarHostState = remember { SnackbarHostState() }
    val wteSnackbarController = rememberWteSnackbarController(snackbarHostState)
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = modifier,
        topBar = {
            WteTopBar(onReturnToFoodChoose, "编辑清单"){ closeMenu ->
                MenuButton("新建表格"){ closeMenu(); editDialogState = EditDialogState.CreateTable }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("重命名表格"){ closeMenu(); editDialogState = EditDialogState.RenameTable }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("删除表格"){ closeMenu(); editDialogState = EditDialogState.DeleteTable }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("导入表格"){
                    closeMenu()
                    try {
                        val input = clipboardManager.getText()?.text
                        if(input == null){
                            wteSnackbarController.showSnackbar("未获取到表格数据")
                            return@MenuButton
                        }
                        onImportTable(input)
                        wteSnackbarController.showSnackbar("已导入表格")
                    } catch(e: SerializationException){
                        e.printStackTrace()
                        wteSnackbarController.showSnackbar("json格式错误")
                    }
                }
                HorizontalDivider(thickness = Dp.Hairline)
                MenuButton("导出表格"){
                    closeMenu()
                    try {
                        val json = onExportTable()
                        clipboardManager.setText(AnnotatedString(json))
                        wteSnackbarController.showSnackbar("已导出json至剪贴板")
                    } catch (e: NoTableSelectedException) {
                        wteSnackbarController.showSnackbar(e.message)
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
        onDeleteTable = {
            onDeleteTable(it)
            wteSnackbarController.showSnackbar("已删除表格")
        },
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
            onReturnToFoodChoose = {},
            onTableSelected = {},
            onRenameTable = { _, _ -> },
            onDeleteTable = {},
            onCreateTable = {},
            onImportTable = {},
            onExportTable = { "" },
            onAddFood = {},
            onDeleteFood = {},
            onClickStar = {},
            onInputName = { _, _ -> },
            onInputWeight = { _, _ -> }
        )
    }
}
