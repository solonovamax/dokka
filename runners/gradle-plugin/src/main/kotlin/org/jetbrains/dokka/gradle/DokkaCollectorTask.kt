package org.jetbrains.dokka.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaBasePlugin.DOCUMENTATION_GROUP
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.lang.IllegalStateException

open class DokkaCollectorTask : DefaultTask() {

    @Input
    var modules: List<String> = emptyList()

    @Input
    var outputDirectory: String = defaultDokkaOutputDirectory().absolutePath

    private lateinit var configuration: GradleDokkaConfigurationImpl

    @Input
    var dokkaTaskNames: Set<String> = setOf()

    @TaskAction
    fun collect() {
        val configurations = project.subprojects
            .filter { subProject -> subProject.name in modules }
            .flatMap { subProject -> dokkaTaskNames.mapNotNull(subProject.tasks::findByName) }
            .filterIsInstance<DokkaTask>()
            .mapNotNull { dokkaTask -> dokkaTask.getConfigurationOrNull() }


        val initial = GradleDokkaConfigurationImpl().apply {
            outputDir = outputDirectory
            cacheRoot = configurations.first().cacheRoot
        }

        // TODO this certainly not the ideal solution
        configuration = configurations.fold(initial) { acc, it: GradleDokkaConfigurationImpl ->
            if (acc.cacheRoot != it.cacheRoot)
                throw IllegalStateException("Dokka task configurations differ on core argument cacheRoot")
            acc.sourceSets = acc.sourceSets + it.sourceSets
            acc.pluginsClasspath = (acc.pluginsClasspath + it.pluginsClasspath).distinct()
            acc
        }
        project.tasks.withType(DokkaTask::class.java).configureEach { it.enforcedConfiguration = configuration }
    }

    init {
        // TODO: This this certainly not the ideal solution
        dokkaTaskNames.forEach { dokkaTaskName ->
            finalizedBy(dokkaTaskName)
        }

        group = DOCUMENTATION_GROUP
    }


}
