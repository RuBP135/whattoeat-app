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
import org.jetbrains.compose.resources.stringResource
import whattoeat.shared.generated.resources.moba
import whattoeat.shared.generated.resources.other_click_10
import whattoeat.shared.generated.resources.other_click_15
import whattoeat.shared.generated.resources.other_click_25
import whattoeat.shared.generated.resources.other_click_5
import whattoeat.shared.generated.resources.other_empty
import whattoeat.shared.generated.resources.other_title
import whattoeat.shared.generated.resources.other_what


@Composable
fun OtherScreen (
    onReturnToHome: () -> Unit
){
    Scaffold(
        topBar = {
            WteTopBar(
                onClickReturn = onReturnToHome,
                title = stringResource(Res.string.other_title)
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

            val text = when {
                cnt < 5 -> stringResource(Res.string.other_empty)
                cnt < 10 -> stringResource(Res.string.other_click_5)
                cnt < 15 -> stringResource(Res.string.other_click_10)
                cnt < 25 -> stringResource(Res.string.other_click_15)
                else -> stringResource(Res.string.other_click_25)
            }

            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge
            )

            ElegantButton(
                text = stringResource(Res.string.other_what)
            ) {
                ++cnt
            }
            if(cnt >= 25) {
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
