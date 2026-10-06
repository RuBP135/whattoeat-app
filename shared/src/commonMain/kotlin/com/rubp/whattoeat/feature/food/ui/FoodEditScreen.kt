package com.rubp.whattoeat.feature.food.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.domain.FoodTableDto
import com.rubp.whattoeat.feature.food.domain.foodTableToJson
import com.rubp.whattoeat.feature.food.domain.jsonToFoodTableDto
import com.rubp.whattoeat.core.components.WteTopBar
import com.rubp.whattoeat.core.components.CardText
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
    onReturnToEat: () -> Unit
) {
    val foods by foodViewModel.foods.collectAsState(initial = emptyList())
    val tables by foodViewModel.tables.collectAsState()
    val currentTable by foodViewModel.currentTable.collectAsState()

    val actions = remember(foodViewModel) {
        object : FoodEditActions {
            override fun onReturnToEat() = onReturnToEat()
            override fun onTableSelected(id: Long) = foodViewModel.switchTable(id)
            override fun onRenameTable(tableId: Long, name: String) = foodViewModel.renameTable(tableId, name)
            override fun onDeleteTable(tableId: Long) = foodViewModel.deleteTable(tableId)
            override fun onCreateTable(name: String) = foodViewModel.createTable(name)
            override fun onImportFoodAndTable(dto: FoodTableDto) = foodViewModel.inputFoodTableDto(dto)
            override fun onAddFood() = foodViewModel.insert(Food(name = "", weight = 1, marked = true))
            override fun onDelFood(food: Food) = foodViewModel.delete(food)
            override fun onClickStar(food: Food) = foodViewModel.update(food.copy(marked = !food.marked))
            override fun onInputName(food: Food, name: String) = foodViewModel.update(food.copy(name = name))
            override fun onInputWeight(food: Food, weight: Int) = foodViewModel.update(food.copy(weight = weight))
        }
    }

    FoodEditContent(
        foods = foods,
        tables = tables,
        currentTable = currentTable,
        actions = actions
    )
}

@Composable
fun FoodEditContent(
    foods: List<Food>,
    tables: List<FoodTable>,
    currentTable: FoodTable?,
    actions: FoodEditActions
) {
    var editDialogState: EditDialogState by remember { mutableStateOf(EditDialogState.None) }
    val tableName = currentTable?.name ?: ""

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() // 获取与当前UI组件生命周期绑定的协程作用域
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            WteTopBar(actions::onReturnToEat, "编辑清单"){ closeMenu ->
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
                            actions.onImportFoodAndTable(foodTableDto)
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ){
                // 标题
                CardText(
                    modifier = Modifier
                        .padding(horizontal = 35.dp)
                        .height(70.dp),
                    text = tableName,
                    style = MaterialTheme.typography.titleLarge,
                    textColor = MaterialTheme.colorScheme.primary
                )

                // 滚动标题栏（切换表格）
                ScrollableTableTitleRow(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth(),
                    selectedTableId = currentTable?.id ?: -1L,
                    tables = tables,
                    onTableSelected = actions::onTableSelected,
                    onAddTable = { editDialogState = EditDialogState.CreateTable } // 拉出创建对话框
                )
                
                // 编辑表
                EditTable(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 30.dp),
                    foods = foods,
                    onClickStar = actions::onClickStar,
                    onInputName = actions::onInputName,
                    onInputWeight = actions::onInputWeight,
                    onClickDelFood = { editDialogState = EditDialogState.DeleteFood(it) },
                )

                Spacer(Modifier.height(25.dp))
                // 底部按钮
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ){
                    WtePrimaryButton(
                        text = "添加新菜品",
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp)
                    ) { actions.onAddFood() }
                    WteSecondaryButton(
                        text = "保存",
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {}
                }
            }

        }
    }

    FoodEditDialogHandler(
        editDialogState,
        tableName,
        currentTable,
        actions,
    ) {
        editDialogState = EditDialogState.None
    }
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
            actions = object : FoodEditActions {
                override fun onReturnToEat() {}
                override fun onTableSelected(id: Long) {}
                override fun onRenameTable(tableId: Long, name: String) {}
                override fun onDeleteTable(tableId: Long) {}
                override fun onCreateTable(name: String) {}
                override fun onImportFoodAndTable(dto: FoodTableDto) {}
                override fun onAddFood() {}
                override fun onDelFood(food: Food) {}
                override fun onClickStar(food: Food) {}
                override fun onInputName(food: Food, name: String) {}
                override fun onInputWeight(food: Food, weight: Int) {}
            }
        )
    }
}
