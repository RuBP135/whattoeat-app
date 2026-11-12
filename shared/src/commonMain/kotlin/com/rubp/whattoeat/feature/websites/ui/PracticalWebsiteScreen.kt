package com.rubp.whattoeat.feature.websites.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.rubp.whattoeat.app.logging.DeduplicatingExceptionLogRecorder
import com.rubp.whattoeat.core.ui.async.UiTaskRunner
import com.rubp.whattoeat.core.ui.components.WteTopBar
import com.rubp.whattoeat.core.ui.components.button.CardButton
import androidx.compose.foundation.lazy.items
import com.rubp.whattoeat.core.ui.feedback.LocalUiFeedback
import com.rubp.whattoeat.feature.websites.model.practicalWebsites
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun PracticalWebsiteScreen(
    recorder: DeduplicatingExceptionLogRecorder,
    onReturnToHome: () -> Unit
){
    val uriHandler = LocalUriHandler.current
    val feedback = LocalUiFeedback.current
    val scope = rememberCoroutineScope()
    val runner = remember(scope, feedback, recorder) {
        UiTaskRunner(
            scope = scope,
            tag = "PracticalWebsiteScreen",
            recorder = recorder,
            onMessage = feedback::showMessage,
            onError = feedback::showError
        )
    }

    PracticalWebsiteContent(
        onReturnToHome = onReturnToHome,
        onOpenWebsite = { url ->
            runner.execute(
                errorLogMessage = "Failed to open practical website"
            ) {
                uriHandler.openUri(url)
            }
        }
    )
}

@Composable
private fun PracticalWebsiteContent(
    onReturnToHome: () -> Unit,
    onOpenWebsite: (String) -> Unit
){
    Scaffold(
        topBar = {
            WteTopBar(
                onClickReturn = onReturnToHome,
                title = "实用网站"
            )
        }
    ) { paddingValues ->

        val modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
        val iconModifier = Modifier.size(36.dp)

        LazyColumn(
            modifier = Modifier
                .width(500.dp)
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(top = 30.dp, start = 30.dp, end = 30.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(practicalWebsites) { item ->
                CardButton(
                    title = item.title,
                    subtitle = item.subtitle,
                    modifier = modifier,
                    icon = {
                        when (val src = item.iconSource) {
                            is ImageVector -> Icon(
                                imageVector = src,
                                contentDescription = null,
                                modifier = iconModifier
                            )
                            is DrawableResource -> Image(
                                painter = painterResource(src),
                                contentDescription = null,
                                modifier = iconModifier
                            )
                        }
                    }
                ){
                    onOpenWebsite(item.url)
                }
            }
        }
    }
}

@Preview
@Composable
fun PracticalWebsiteScreenPreview(){
    PracticalWebsiteContent(
        onReturnToHome = {},
        onOpenWebsite = {}
    )
}
