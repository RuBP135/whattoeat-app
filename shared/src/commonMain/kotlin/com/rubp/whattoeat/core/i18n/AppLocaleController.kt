package com.rubp.whattoeat.core.i18n

/**
 * 把应用语言应用到平台层，使 `stringResource` 能取出对应语言的资源。
 *
 * Android 立即可见；iOS 需下次启动才生效。
 *
 * @param language 目标语言。
 */
expect fun applyAppLanguage(language: AppLanguage)