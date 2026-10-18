package com.rubp.whattoeat.core.i18n

import platform.Foundation.NSUserDefaults

actual fun applyAppLanguage(language: AppLanguage) {
    // iOS 的 NSLocale.preferredLanguages 在进程内不会变，只能影响下次启动
    NSUserDefaults.standardUserDefaults.setObject(listOf(language.tag), forKey = "AppleLanguages")
}