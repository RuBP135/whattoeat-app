package com.rubp.whattoeat.feature.websites.model

import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Search
import com.composables.icons.materialicons.filled.Add_task
import com.composables.icons.materialicons.filled.Filter
import com.composables.icons.materialicons.filled.Network_cell
import com.composables.icons.materialicons.filled.Public
import com.composables.icons.materialicons.filled.Saved_search
import com.composables.icons.materialicons.outlined.Account_balance
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.bilibili
import org.jetbrains.compose.resources.stringResource
import androidx.compose.runtime.Composable
import whattoeat.shared.generated.resources.website_bilibili_subtitle
import whattoeat.shared.generated.resources.website_cet_subtitle
import whattoeat.shared.generated.resources.website_cet_title
import whattoeat.shared.generated.resources.website_course_subtitle
import whattoeat.shared.generated.resources.website_course_title
import whattoeat.shared.generated.resources.website_hall_subtitle
import whattoeat.shared.generated.resources.website_hall_title
import whattoeat.shared.generated.resources.website_ip_subtitle
import whattoeat.shared.generated.resources.website_ip_title
import whattoeat.shared.generated.resources.website_labor_subtitle
import whattoeat.shared.generated.resources.website_labor_title
import whattoeat.shared.generated.resources.website_power_subtitle
import whattoeat.shared.generated.resources.website_power_title
import whattoeat.shared.generated.resources.website_practice_subtitle
import whattoeat.shared.generated.resources.website_practice_title

@Composable
fun practicalWebsites(): List<WebsiteItem> = listOf(
    WebsiteItem(
        title = stringResource(Res.string.website_hall_title),
        subtitle = stringResource(Res.string.website_hall_subtitle),
        url = "https://ehall.xidian.edu.cn/",
        iconSource = MaterialIcons.Outlined.Account_balance
    ),
    WebsiteItem(
        title = stringResource(Res.string.website_power_title),
        subtitle = stringResource(Res.string.website_power_subtitle),
        url = "https://ignypt.xidian.edu.cn/revenueH5/mainPage",
        iconSource = MaterialIcons.Filled.Public
    ),
    WebsiteItem(
        title = stringResource(Res.string.website_labor_title),
        subtitle = stringResource(Res.string.website_labor_subtitle),
        url = "https://xgxt.xidian.edu.cn/xsfw/sys/ldjyappxidian/*default/index.do#/wdjykc",
        iconSource = MaterialIcons.Filled.Search
    ),
    WebsiteItem(
        title = stringResource(Res.string.website_cet_title),
        subtitle = stringResource(Res.string.website_cet_subtitle),
        url = "http://cet-bm.neea.edu.cn",
        iconSource = MaterialIcons.Filled.Add_task
    ),
    WebsiteItem(
        title = stringResource(Res.string.website_course_title),
        subtitle = stringResource(Res.string.website_course_subtitle),
        url = "http://xk.xidian.edu.cn",
        iconSource = MaterialIcons.Filled.Filter
    ),
    WebsiteItem(
        title = stringResource(Res.string.website_practice_title),
        subtitle = stringResource(Res.string.website_practice_subtitle),
        url = "https://jinshuju.com/f/Yc2cNy/s/tAkU85",
        iconSource = MaterialIcons.Filled.Saved_search
    ),
    WebsiteItem(
        title = "Bilibili",
        subtitle = stringResource(Res.string.website_bilibili_subtitle),
        url = "https://www.bilibili.com/",
        iconSource = Res.drawable.bilibili
    ),
    WebsiteItem(
        title = stringResource(Res.string.website_ip_title),
        subtitle = stringResource(Res.string.website_ip_subtitle),
        url = "https://ping0.cc/",
        iconSource = MaterialIcons.Filled.Network_cell
    )
)
