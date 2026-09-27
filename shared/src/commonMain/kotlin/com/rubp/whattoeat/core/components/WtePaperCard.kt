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

@Composable
fun WtePaperCard(
    modifier: Modifier,
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
            verticalArrangement = Arrangement.spacedBy(6.dp)
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