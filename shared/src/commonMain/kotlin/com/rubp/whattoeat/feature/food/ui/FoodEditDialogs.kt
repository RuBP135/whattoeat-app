package com.rubp.whattoeat.feature.food.ui

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
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.core.components.ConfirmDialog
import com.rubp.whattoeat.core.components.EditDialog
import org.jetbrains.compose.resources.stringResource
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.food_dialog_new_table
import whattoeat.shared.generated.resources.food_dialog_table_name
import whattoeat.shared.generated.resources.food_dialog_rename_table
import whattoeat.shared.generated.resources.food_dialog_new_table_name
import whattoeat.shared.generated.resources.food_dialog_delete_table
import whattoeat.shared.generated.resources.food_dialog_delete_confirm
import whattoeat.shared.generated.resources.food_dialog_delete_dish
import whattoeat.shared.generated.resources.food_dialog_help_title
import whattoeat.shared.generated.resources.food_dialog_help_selected
import whattoeat.shared.generated.resources.food_dialog_help_delete
import whattoeat.shared.generated.resources.food_dialog_help_import_export
import whattoeat.shared.generated.resources.food_dialog_help_ok

@Composable
fun FoodEditDialogHandler(
    dialogState: EditDialogState,
    tableName: String,
    currentTable: FoodTable?,
    onCreateTable: (String) -> Unit,
    onRenameTable: (Long, String) -> Unit,
    onDeleteTable: (Long) -> Unit,
    onDeleteFood: (Food) -> Unit,
    onDismiss: () -> Unit
) {
    when(dialogState){
        // 无对话框
        is EditDialogState.None -> return

        // 弹出创建新表格对话框
        is EditDialogState.CreateTable -> {
            EditDialog(
                title = stringResource(Res.string.food_dialog_new_table),
                labelText = stringResource(Res.string.food_dialog_table_name),
                onConfirm = { name ->
                    onCreateTable(name)
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        // 弹出修改表格对话框
        is EditDialogState.RenameTable -> {
            EditDialog(
                title = stringResource(Res.string.food_dialog_rename_table),
                initialText = tableName,
                labelText = stringResource(Res.string.food_dialog_new_table_name),
                onConfirm = { newName ->
                    currentTable?.let { onRenameTable(it.id, newName) }
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        // 弹出删除表格对话框
        is EditDialogState.DeleteTable -> {
            ConfirmDialog(
                title = stringResource(Res.string.food_dialog_delete_table),
                message = stringResource(Res.string.food_dialog_delete_confirm),
                onConfirm = {
                    currentTable?.id?.let { onDeleteTable(it) }
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        is EditDialogState.DeleteFood -> {
            ConfirmDialog(
                title = stringResource(Res.string.food_dialog_delete_dish),
                message = stringResource(Res.string.food_dialog_delete_confirm),
                onConfirm = {
                    onDeleteFood(dialogState.food)
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
                            Text(stringResource(Res.string.food_dialog_help_title))
                        }
                        HorizontalDivider()
                        Text(
                            text = stringResource(Res.string.food_dialog_help_selected),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(Res.string.food_dialog_help_delete),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(Res.string.food_dialog_help_import_export),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ){
                            TextButton(onClick = onDismiss){
                                Text(stringResource(Res.string.food_dialog_help_ok ))
                            }
                        }
                    }

                }
            }

        }
    }
}