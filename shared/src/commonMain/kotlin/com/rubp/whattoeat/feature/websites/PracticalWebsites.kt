package com.rubp.whattoeat.feature.websites

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

val practicalWebsites = listOf(
    WebsiteItem(
        title = "一站式服务大厅",
        subtitle = "泥电一站式服务大厅...",
        url = "https://ehall.xidian.edu.cn/",
        iconSource = MaterialIcons.Outlined.Account_balance
    ),
    WebsiteItem(
        title = "电费网站",
        subtitle = "Queen提供的电费网站",
        url = "https://ignypt.xidian.edu.cn/revenueH5/mainPage",
        iconSource = MaterialIcons.Filled.Public
    ),
    WebsiteItem(
        title = "劳动教育查询",
        subtitle = "查看劳动教育学时是否已满",
        url = "https://xgxt.xidian.edu.cn/xsfw/sys/ldjyappxidian/*default/index.do#/wdjykc",
        iconSource = MaterialIcons.Filled.Search
    ),
    WebsiteItem(
        title = "四六级",
        subtitle = "会赢的",
        url = "http://cet-bm.neea.edu.cn",
        iconSource = MaterialIcons.Filled.Add_task
    ),
    WebsiteItem(
        title = "选课",
        subtitle = "别睡了，该起床抢课了",
        url = "http://xk.xidian.edu.cn",
        iconSource = MaterialIcons.Filled.Filter
    ),
    WebsiteItem(
        title = "社会实践项目查询",
        subtitle = "看看自己社会实践做了几个？",
        url = "https://jinshuju.com/f/Yc2cNy/s/tAkU85",
        iconSource = MaterialIcons.Filled.Saved_search
    ),
    WebsiteItem(
        title = "Bilibili",
        subtitle = "甚至B站",
        url = "https://www.bilibili.com/",
        iconSource = Res.drawable.bilibili
    ),
    WebsiteItem(
        title = "IP纯净度测试",
        subtitle = "测测你的IP是否干净",
        url = "https://ping0.cc/",
        iconSource = MaterialIcons.Filled.Network_cell
    )
)
