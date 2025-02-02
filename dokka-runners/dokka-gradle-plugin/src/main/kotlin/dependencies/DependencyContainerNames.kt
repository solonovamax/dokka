/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
package org.jetbrains.dokka.gradle.dependencies

import org.gradle.api.artifacts.Configuration
import org.jetbrains.dokka.gradle.DokkaBasePlugin
import org.jetbrains.dokka.gradle.DokkaBasePlugin.Companion.DOKKA_CONFIGURATION_NAME
import org.jetbrains.dokka.gradle.DokkaBasePlugin.Companion.DOKKA_GENERATOR_PLUGINS_CONFIGURATION_NAME
import org.jetbrains.dokka.gradle.internal.DokkaInternalApi
import org.jetbrains.dokka.gradle.internal.HasFormatName

/**
 * Names of the Gradle [Configuration]s used by the [Dokka Plugin][DokkaBasePlugin].
 *
 * Beware the confusing terminology:
 * - [Gradle Configurations][org.gradle.api.artifacts.Configuration] - share files between subprojects. Each has a name.
 * - [DokkaConfiguration][org.jetbrains.dokka.DokkaConfiguration] - parameters for executing the Dokka Generator
 */
@DokkaInternalApi
class DependencyContainerNames(override val formatName: String) : HasFormatName() {

    val dokka = DOKKA_CONFIGURATION_NAME.appendFormat()

    /**
     * ### Dokka Plugins
     *
     * Includes transitive dependencies, so this can be passed to the Dokka Generator Worker classpath.
     *
     * Will be used in user's build scripts to declare additional format-specific Dokka Plugins.
     */
    val pluginsClasspath = DOKKA_GENERATOR_PLUGINS_CONFIGURATION_NAME.appendFormat()

    /**
     * ### Dokka Plugins (excluding transitive dependencies)
     *
     * Will be used to create Dokka Generator Parameters
     *
     * Extends [pluginsClasspath]
     *
     * Internal Dokka Gradle Plugin usage only.
     */
    val pluginsClasspathIntransitiveResolver =
        "${dokka}PluginsClasspathIntransitiveResolver"

    /**
     * ### Dokka Generator Classpath
     *
     * Classpath used to execute the Dokka Generator.
     *
     * Extends [pluginsClasspath], so Dokka plugins and their dependencies are included.
     */
    val generatorClasspath = "${dokka}GeneratorClasspath"

    /** Resolver for [generatorClasspath] - internal Dokka usage only. */
    val generatorClasspathResolver = "${dokka}GeneratorClasspathResolver"

    val publicationPluginClasspath = "${dokka}PublicationPluginClasspath"
    val publicationPluginClasspathApiOnly = "${publicationPluginClasspath}ApiOnly"
    val publicationPluginClasspathResolver = "${publicationPluginClasspath}Resolver"
    val publicationPluginClasspathApiOnlyConsumable =
        "${publicationPluginClasspathApiOnly}Consumable"
}
