package com.rubp.whattoeat.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme

/**
 * 圆角纸感卡片，提供纵向布局
 *
 * @param verticalArrangement 允许修改垂直排列参数
 */
@Composable
fun WtePaperCard(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(6.dp), // 控制内容的垂直排列与间距
    content: @Composable ColumnScope.() -> Unit
){
    Surface(
        modifier = modifier,
        color = WteTheme.extendedColors.paper,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            color = WteTheme.extendedColors.paperBorder
        )
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = verticalArrangement
        ){
            content()
        }
    }
}

@Preview
@Composable
private fun WtePaperCardPreview(){
    WhatToEatPreviewTheme{
        WtePaperCard(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
        ) {}
    }
}