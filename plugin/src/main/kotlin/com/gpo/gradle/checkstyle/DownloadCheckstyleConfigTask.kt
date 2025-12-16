package com.gpo.gradle.checkstyle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class DownloadCheckstyleConfigTask : DefaultTask() {

    @get:Input
    abstract val version: Property<String>

    @get:Input
    abstract val baseUrl: Property<String>

    @get:OutputDirectory
    abstract val configDir: DirectoryProperty

    @TaskAction
    fun download() {
        val configDirFile = configDir.get().asFile

        if (!configDirFile.exists()) {
            configDirFile.mkdirs()
            logger.info("Created checkstyle config directory: $configDirFile")
        }

        val versionPath = version.get()
        val base = baseUrl.get()

        val checkstyleUrl = "$base/$versionPath/checkstyle/checkstyle.xml"
        val suppressionsUrl = "$base/$versionPath/checkstyle/suppressions.xml"

        downloadFile(checkstyleUrl, configDirFile.resolve("checkstyle.xml"))
        downloadFile(suppressionsUrl, configDirFile.resolve("suppressions.xml"))

        logger.lifecycle("Successfully downloaded checkstyle configs to: $configDirFile")
    }

    private fun downloadFile(url: String, destination: java.io.File) {
        try {
            logger.info("Downloading: $url")

            ant.invokeMethod("get", mapOf(
                "src" to url,
                "dest" to destination.absolutePath,
                "verbose" to false
            ))

            logger.info("Downloaded to: ${destination.absolutePath}")
        } catch (e: Exception) {
            throw RuntimeException("Failed to download $url: ${e.message}", e)
        }
    }
}
