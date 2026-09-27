# WhatToEat2 项目 AI 辅助开发约束规范

## 1. 项目概览与核心业务
本项目 `WhatToEat2` 是一款基于 Compose Multiplatform 开发的跨端应用（同时支持 Android 与 iOS）。
其核心功能包括：获取云端存储的点评以及食物数据（包含评分、图片、标签、位置），按食物名称和tag搜寻食物，对食物进行点评，以及实验网站快捷入口、随机抽取一个食物的一些小功能。

## 2. 目录结构与隔离红线
- **主战场 (`commonMain`)**：所有的业务逻辑、网络请求、数据持久化以及 Compose UI 必须写在 `commonMain` 源码集中。
- **绝对红线**：严禁在 `commonMain` 内导入任何 `java.*` 或 `android.*` 前缀的包。如果必须调用底层硬件能力，强制要求使用 Kotlin 的 `expect/actual` 机制进行抽象。
- **宿主结构**：应用的 Android 端入口以 `MainActivity` 作为顶级宿主进行承载；如果依然保留了早期的视图结构，子页面使用宿主内的路由或组件进行分发，但 UI 渲染全权交由 Compose Multiplatform 引擎。

## 3. 架构与状态管理 (MVVM)

- **多端 ViewModel**：目前项目全面拥抱官方的 Multiplatform ViewModel，因此 `ViewModel` 类可以直接在 `commonMain` 中定义并使用。
- **响应式数据流**：强约束使用 `StateFlow` 进行状态管理。UI 层不可突变（Immutable），ViewModel 内部通过 `MutableStateFlow` 维护状态，UI 层通过收集 Flow 驱动重组。

## 4. 本地数据持久化
对于菜品、历史记录等本地数据的管理：

- **框架指定**：数据库使用支持 KMP 版本的 **Room**（Room 2.8.4），轻量可持久化数据（如一些配置和需要记忆的内容）使用 **Multiplatform Settings**（Multiplatform Settings1.2.0）。
- **代码位置**：所有的 `@Entity` 数据表结构、`@Dao` 数据访问对象以及 `@Database` 抽象类，全部放置在 `commonMain` 中运行在双端。不要向我推荐 SQLDelight，除非我明确要求。
- **数据流**：采用 database/settings -> Repository -> ViewModel -> UI

## 5. UI 美学与交互规范 (Compose)
- **动画与视觉**：对于食物随机抽取的 2 秒滚动效果，请使用 Compose 的 `Animatable` 协程动画或 `suspend` 函数配合 `Flow` 定时发射来修改文本状态，以确保高帧率。
- **布局一致性**：要求 UI 代码整洁对称。例如在水平布局 `Row` 中处理头像与食物名称时，必须熟练使用 `Alignment.CenterVertically` 参数确保跨端视觉完美居中。
- **参数说明要求**：当提供 UI 组件或新功能的实现代码时，必须列出使用的方法名，并在注释中简明扼要地标明各参数的作用。

## 6. AI 搜索要求
由于 Compose Multiplatform 技术栈迭代极快，遇到不确定的依赖库（如跨端图片加载 Coil3、跨端网络 Ktor）的 API 细节时，必须主动进行联网搜索，不准依靠旧有记忆胡编乱造。

## 7.依赖引入要求

需要引入新的依赖时候，必须明确告知。

## 8.命名规范

变量名、函数名、类名等应该与仓库原有代码的风格一致，不能混搭

## 9.指令规范

不可执行rm -rf等危险指令，执行rm等指令前必须强调

## 10.文件规范

单个文件不宜太长，尽量在250行以下

## 11.计划新功能

提出修改和新增新功能的时候，若有表述不清晰的部分，必须进行追问，不可自己捏造细节。需等所有细节都确认清楚，且被接受了，才可开始动工。

## 12.修改代码

修改或者增加代码的时候，若要修改其他文件的代码，必须进行强调，而且非必要不进行修改

## 13.UI 视觉规范

- 修改或新增任何 Compose UI 前，必须先阅读 `docs/design/design-system.md`。
- `docs/design/design-system.md` 是项目 UI 视觉设计的事实来源；除非用户明确要求改变设计方向，否则不得偏离。
- 设计方向发生变化时，应先更新视觉规范文档，再修改具体页面代码。

