package com.rubp.whattoeat.feature.food.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import kotlin.collections.forEach

// --- 书签侧栏 ---

@Composable
fun BookmarkSidebar(
    tables: List<FoodTable>,
    currentTable: FoodTable?,
    onTableSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedTableId by remember { mutableStateOf(-1L) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 100.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.End
    ) {
        tables.forEach { table ->
            BookmarkItem(
                name = table.name,
                isActive = table.id == currentTable?.id,
                isExpanded = table.id == expandedTableId,
                onClick = {
                    expandedTableId = if (expandedTableId == table.id) {
                        -1L // 已展开，再次点击：切换到此表
                    } else {
                        table.id // 未展开：展开显示完整表名
                    }
                    onTableSelected(table.id)
                }
            )
        }
    }
}

@Composable
private fun BookmarkItem(
    name: String,
    isActive: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val collapsedWidth = 36.dp
    // 根据文字长度估算展开宽度（每个中文字符约 16dp）
    val expandedWidthDp = with(LocalDensity.current) {
        // 中文一个字约 1em = bodySmall 字号 ≈ 12sp，加上 padding
        val charCount = name.length.coerceIn(2, 8)
        (charCount * 16 + 16).coerceIn(56, 160).dp
    }
    val width by animateDpAsState(
        targetValue = if (isExpanded) expandedWidthDp else collapsedWidth,
        animationSpec = tween(durationMillis = 250),
        label = "bookmarkWidth"
    )

    Surface(
        modifier = Modifier
            .width(width)
            .height(40.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
        color = if (isActive) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 6.dp, end = 6.dp)
        ) {
            Text(
                text = if (isExpanded) name else name.take(2),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isActive) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}