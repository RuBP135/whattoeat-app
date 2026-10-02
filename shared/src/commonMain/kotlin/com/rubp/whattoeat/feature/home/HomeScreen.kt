package com.rubp.whattoeat.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.app.navigation.MainBottomBarSpacer
import com.rubp.whattoeat.core.components.CardButton
import com.rubp.whattoeat.core.components.PrimaryButton
import com.rubp.whattoeat.core.components.WtePaperCard
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme
import org.jetbrains.compose.resources.painterResource
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.what_to_eat_wordmark


@Composable
fun HomeScreen(
    onNavigateToEat: () -> Unit,
    onNavigateToPracticalWebsite: () -> Unit,
    onNavigateToOther: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        containerColor = WteTheme.extendedColors.brandBackdrop,
        bottomBar = { MainBottomBarSpacer() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            HomeHeader()

            PrimaryActionCard(onClick = onNavigateToEat)

            SecondaryActionsCard(
                onNavigateToPracticalWebsite = onNavigateToPracticalWebsite,
                onOpenSpeedTest = { uriHandler.openUri("https://test.xidian.edu.cn") },
                onNavigateToOther = onNavigateToOther
            )
        }
    }
}

@Composable
private fun HomeHeader() {
    Image(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .fillMaxWidth(),
        painter = painterResource(Res.drawable.what_to_eat_wordmark),
        contentDescription = "What To Eat",
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
    )
}

/**
 * 首页主功能卡片。
 *
 * @param onClick 点击“开始抽取”时进入随机选餐页面。
 */
@Composable
private fun PrimaryActionCard(onClick: () -> Unit) {
    WtePaperCard(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "今天吃什么",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "从当前清单中随机抽取一项",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        PrimaryButton(
            text = "开始抽取",
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            textColor = MaterialTheme.colorScheme.onPrimary,
            onClick = onClick
        )
    }
}

/**
 * 首页次级功能入口。
 *
 * @param onNavigateToPracticalWebsite 进入实用网站页面。
 * @param onOpenSpeedTest 打开校园网测速网站。
 * @param onNavigateToOther 进入其他功能页面。
 */
@Composable
private fun SecondaryActionsCard(
    onNavigateToPracticalWebsite: () -> Unit,
    onOpenSpeedTest: () -> Unit,
    onNavigateToOther: () -> Unit
) {
    WtePaperCard(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
    ) {
        CardButton(
            title = "实用网站",
            subtitle = "查看常用服务与工具入口",
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToPracticalWebsite
        )
        HorizontalDivider(color = WteTheme.extendedColors.paperInnerLine)
        CardButton(
            title = "测网速",
            subtitle = "检查当前校园网络速度",
            modifier = Modifier.fillMaxWidth(),
            onClick = onOpenSpeedTest
        )
        HorizontalDivider(color = WteTheme.extendedColors.paperInnerLine)
        CardButton(
            title = "其他",
            subtitle = "查看其他功能",
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToOther
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WhatToEatPreviewTheme {
        HomeScreen(
            onNavigateToEat = {},
            onNavigateToPracticalWebsite = {},
            onNavigateToOther = {}
        )
    }
}
