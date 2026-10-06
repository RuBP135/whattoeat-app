package com.rubp.whattoeat.feature.food.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Delete
import com.rubp.whattoeat.core.components.WteLeftSwipeBox
import com.rubp.whattoeat.feature.food.data.entity.Food
import org.jetbrains.compose.resources.painterResource
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.filled_star
import whattoeat.shared.generated.resources.outlined_star


@Composable
internal fun FoodEditRow(
    modifier: Modifier = Modifier,
    food: Food,
    onClickStar: () -> Unit,
    onInputName: (String) -> Unit,
    onInputWeight: (Int) -> Unit,
    onDelete: () -> Unit
) {
    var foodName by remember(food.id) { mutableStateOf(TextFieldValue(food.name)) }
    var foodWeight by remember(food.id) { mutableStateOf(TextFieldValue(food.weight.toString())) }
    val isWeightError = foodWeight.text.isNotEmpty() && foodWeight.text.toFoodWeightOrNull() == null

    WteLeftSwipeBox(
        modifier = modifier,
        menuWidthDp = 60.dp,
        menuContent = {
            FilledTonalIconButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = onDelete,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(MaterialIcons.Filled.Delete, contentDescription = "删除此行数据")
            }
        }
    ) { foregroundModifier ->
        Surface(
            modifier = foregroundModifier.heightIn(min = 72.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            FoodTableRow(
                selection = {
                    IconToggleButton(
                        checked = food.marked,
                        onCheckedChange = { onClickStar() }
                    ) {
                        Image(
                            painter = painterResource(
                                if (food.marked) Res.drawable.filled_star
                                else Res.drawable.outlined_star
                            ),
                            contentDescription = "参选"
                        )
                    }
                },
                name = {
                    FoodEditTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = foodName,
                        onValueChange = {
                            foodName = it
                            onInputName(it.text)
                        },
                        shape = RoundedCornerShape(4.dp),
                        placeholder = "请输入名称"
                    )
                },
                weight = {
                    FoodEditTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = foodWeight,
                        onValueChange = {
                            foodWeight = it
                            it.text.toFoodWeightOrNull()?.let(onInputWeight)
                        },
                        shape = RoundedCornerShape(6.dp),
                        isError = isWeightError,
                        keyboardType = KeyboardType.Number
                    )
                }
            )
        }
    }
}

@Composable
private fun FoodEditTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    shape: Shape,
    placeholder: String? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            errorContainerColor = containerColor
        ),
        placeholder = placeholder?.let { text ->
            { Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        },
        isError = isError,
        supportingText = if (isError) {
            { Text("请输入有效数字", color = MaterialTheme.colorScheme.error) }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

private fun String.toFoodWeightOrNull(): Int? =
    takeIf { it.length in 1..5 && it.all(Char::isDigit) }?.toIntOrNull()
