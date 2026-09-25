package com.rubp.whattoeat.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


@Composable
fun BoxText(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    textColor: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current
){
    Box(
        modifier = modifier.background(color = backgroundColor), // 背景颜色
        contentAlignment = Alignment.Center // 默认正中间
    ){
        Text(
            text = text,
            color = textColor,
            style = style
        )
    }
}

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

@Composable
fun CardTextFiled(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    textColor: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    textAlign: Alignment = Alignment.CenterStart, // 默认左边中间，因为卡片应该从左往右读
    onValueChange: (String) -> Unit
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(backgroundColor)
    ){
        Box(
            modifier = Modifier.padding(5.dp),
            contentAlignment = textAlign
        ){
            OutlinedTextField(
                value = text,
                onValueChange = onValueChange,
                colors = OutlinedTextFieldDefaults.colors(textColor)  ,
                textStyle = style,
            )
        }

    }
}
