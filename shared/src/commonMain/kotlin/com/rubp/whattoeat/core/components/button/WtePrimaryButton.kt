package com.rubp.whattoeat.core.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WteTheme

@Composable
fun WtePrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.Unspecified,
    onClick: () -> Unit
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor),
        border = BorderStroke(1.dp, WteTheme.extendedColors.paperBorder),
        onClick = onClick
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
        }
    }
}