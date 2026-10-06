package com.rubp.whattoeat.feature.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import whattoeat.shared.generated.resources.Res
import com.rubp.whattoeat.core.components.WteTopBar
import com.rubp.whattoeat.core.components.button.ElegantButton
import whattoeat.shared.generated.resources.moba

@Composable
fun OtherScreen (
    onReturnToHome: () -> Unit
){
    Scaffold(
        topBar = {
            WteTopBar(
                onClickReturn = onReturnToHome,
                title = "其他"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 100.dp, start = 30.dp, end = 30.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            var cnt by remember { mutableStateOf(0) }
            var text by remember { mutableStateOf("这里什么都没有") }

            if(cnt in 5..<10) text = "为何你还在点击"
            else if(cnt < 15) text = "真什么都没有"
            else if(cnt < 25) text = "已经到头了，确实什么都没有"
            else text = "你赢了，必须moba！"


            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge
            )


            ElegantButton(
                text = "这是什么？"
            ) {
                ++cnt
            }

            if(cnt >= 25){
                AsyncImage(
                    model = Res.drawable.moba,
                    contentDescription = "moba"
                )
            }
        }
    }
}

@Preview
@Composable
fun OtherScreenPreview(){
    OtherScreen{}
}
