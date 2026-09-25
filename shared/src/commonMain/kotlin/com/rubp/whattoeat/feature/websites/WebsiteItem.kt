package com.rubp.whattoeat.feature.websites

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

sealed interface IconSource {
    data class Vector(val icon: ImageVector): IconSource
    data class Resource(val res: DrawableResource): IconSource
}
data class WebsiteItem(
    val title: String,
    val subtitle: String,
    val url: String,
    val iconSource: Any // ImageVector 或 Int（drawable 资源 id）
)
