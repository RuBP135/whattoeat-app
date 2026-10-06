package com.rubp.whattoeat.core.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
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
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(6.dp),
    content: @Composable ColumnScope.() -> Unit
){
    Box(
        modifier = modifier,
        propagateMinConstraints = true
    ){

        Box(
            modifier = Modifier
                .matchParentSize()
                .absoluteOffset(
                    x = 4.dp,
                    y = 4.dp
                )
                .background(
                    color = WteTheme.extendedColors.offsetShadow,
                    shape = RoundedCornerShape(20.dp)
                )
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
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