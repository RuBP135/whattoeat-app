package com.rubp.whattoeat.ui.screens.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.outlined.Help
import com.rubp.whattoeat.data.local.entry.FoodTable
import com.rubp.whattoeat.core.components.ConfirmDialog
import com.rubp.whattoeat.core.components.EditDialog

@Composable
fun FoodEditDialogHandler(
    dialogState: EditDialogState,
    tableName: String,
    currentTable: FoodTable?,
    actions: FoodEditActions,
    onDismiss: () -> Unit
) {
    when(dialogState){
        // 无对话框
        is EditDialogState.None -> return

        // 弹出创建新表格对话框
        is EditDialogState.CreateTable -> {
            EditDialog(
                title = "新建表格",
                labelText = "表格名称",
                onConfirm = { name ->
                    actions.onCreateTable(name)
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        // 弹出修改表格对话框
        is EditDialogState.RenameTable -> {
            EditDialog(
                title = "修改表格名称",
                initialText = tableName,
                labelText = "新的表格名称",
                onConfirm = { newName ->
                    currentTable?.let { actions.onRenameTable(it.id, newName) }
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        // 弹出删除表格对话框
        is EditDialogState.DeleteTable -> {
            ConfirmDialog(
                title = "删除表格",
                message = "确认删除？",
                onConfirm = {
                    currentTable?.id?.let { actions.onDeleteTable(it) }
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        is EditDialogState.DeleteFood -> {
            ConfirmDialog(
                title = "删除菜品",
                message = "确认删除？",
                onConfirm = {
                    actions.onDelFood(dialogState.food)
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        is EditDialogState.Help -> {
            Dialog(
                onDismissRequest = onDismiss
            ){
                Surface (
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    shadowElevation = 6.dp,
                    modifier = Modifier
                ){
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()), // 如果内容多了，可以滚动
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = MaterialIcons.Outlined.Help,
                                contentDescription = null
                            )
                            Text("帮助")
                        }
                        HorizontalDivider()
                        Text(
                            text = "参选：点击星星可以开关是否选择该食物，未被选择的食物不会进入随机选择列表",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "删除食物：对食物卡片向左滑动可以看到删除按钮",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "导入导出表格：导出表格时，会将内容复制到手机剪贴板上；导入表格时，会从手机剪贴板获取内容（因此您需要确保已经复制了别人发送给您的表格，才能正确导入）",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ){
                            TextButton(onClick = onDismiss){
                                Text("知道了")
                            }
                        }
                    }

                }
            }

        }
    }
}