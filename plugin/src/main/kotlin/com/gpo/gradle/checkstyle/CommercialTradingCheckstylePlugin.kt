package com.gpo.gradle.checkstyle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin

class CommercialTradingCheckstylePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(CheckstylePlugin::class.java)

        val extension = project.extensions.create(
            "commercialTradingCheckstyle",
            CommercialTradingCheckstyleExtension::class.java
        )

        extension.configDir.convention(
            project.layout.projectDirectory.dir("config/checkstyle")
        )

        val downloadTask = project.tasks.register(
            "downloadCheckstyleConfig",
            DownloadCheckstyleConfigTask::class.java
        ) {
            group = "verification"
            description = "Downloads checkstyle configuration files from GitHub"

            version.set(extension.version)
            baseUrl.set(extension.baseUrl)
            configDir.set(extension.configDir)

            outputs.file(extension.configDir.file("checkstyle.xml"))
            outputs.file(extension.configDir.file("suppressions.xml"))
        }

        project.afterEvaluate {
            if (extension.autoDownload.get()) {
                downloadTask.get().download()
            }

            val checkstyleExt = project.extensions.getByType(CheckstyleExtension::class.java)
            checkstyleExt.toolVersion = extension.checkstyleVersion.get()
            checkstyleExt.configDirectory.set(extension.configDir)
            checkstyleExt.maxErrors = extension.maxErrors.get()
            checkstyleExt.maxWarnings = extension.maxWarnings.get()
            checkstyleExt.isIgnoreFailures = false
            checkstyleExt.isShowViolations = true

            project.tasks.withType(Checkstyle::class.java).configureEach {
                mustRunAfter(downloadTask)

                extension.excludePatterns.get().forEach { pattern ->
                    exclude(pattern)
                }

                configFile = extension.configDir.file("checkstyle.xml").get().asFile
            }
        }
    }
}
