package com.rubp.whattoeat.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TitleCard(
    title: String,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)
){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
    ){
        Column(
            modifier = Modifier.width(IntrinsicSize.Max) // 保证有个width，这样title的fillMaxWidth就不会因为外部容器都没限制而撑满
        ){
            BoxText(
                text = title,
                modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 45.dp),
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            // 内容
            content()
        }
    }
}



@Composable
fun RowItem(
    modifier: Modifier = Modifier,
    cells :List<Cell>
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ){
        for(cell in cells) {
            Box(
                modifier = Modifier.weight(cell.weight),
                contentAlignment = Alignment.Center
            ){
                cell.content()
            }
        }
    }
}

