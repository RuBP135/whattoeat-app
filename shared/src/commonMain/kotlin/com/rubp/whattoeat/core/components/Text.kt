package com.rubp.whattoeat.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


@Composable
fun CardText(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    textColor: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    textAlign: Alignment = Alignment.CenterStart // 默认左边中间，因为卡片应该从左往右读
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(backgroundColor)
    ){
        Box(
            modifier = Modifier.fillMaxSize().padding(5.dp),
            contentAlignment = textAlign
        ){
            Text(
                text = text,
                color = textColor,
                style = style
            )
        }

    }
}
