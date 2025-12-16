package com.gpo.gradle.checkstyle

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class CommercialTradingCheckstyleExtension {
    abstract val version: Property<String>

    abstract val configDir: DirectoryProperty

    abstract val autoDownload: Property<Boolean>

    abstract val baseUrl: Property<String>

    abstract val excludePatterns: ListProperty<String>

    abstract val checkstyleVersion: Property<String>

    abstract val maxErrors: Property<Int>

    abstract val maxWarnings: Property<Int>

    init {
        version.convention("main")
        autoDownload.convention(true)
        baseUrl.convention("https://raw.githubusercontent.com/global-engineering-shared/commercial-trading-checkstyle/refs/heads")
        checkstyleVersion.convention("10.25.0")
        maxErrors.convention(0)
        maxWarnings.convention(0)
    }
}
