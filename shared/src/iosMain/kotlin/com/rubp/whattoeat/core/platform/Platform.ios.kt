package com.rubp.whattoeat.core.platform

import platform.UIKit.UIDevice
import com.rubp.whattoeat.core.Platform

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()