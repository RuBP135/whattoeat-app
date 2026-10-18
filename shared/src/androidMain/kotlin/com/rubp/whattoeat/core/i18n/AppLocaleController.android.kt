package com.rubp.whattoeat.core.i18n

import java.util.Locale

actual fun applyAppLanguage(language: AppLanguage) {
    // compose-resources 在 Android 上通过 LocaleList.getDefault() 读取平台语言
    Locale.setDefault(Locale.forLanguageTag(language.tag))
}