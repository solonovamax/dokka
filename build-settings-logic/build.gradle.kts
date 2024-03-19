import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    `kotlin-dsl`
}

description = "Conventions for use in settings.gradle.kts scripts"

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(libs.gradlePlugin.gradle.enterprise)
    implementation(libs.gradlePlugin.gradle.customUserData)
}

tasks.withType<KotlinCompile>().configureEach {
    @OptIn(InternalKotlinGradlePluginApi::class)
    doFirst {
        println("[$path] classpathSnapshotProperties.useClasspathSnapshot.orNull ${classpathSnapshotProperties.useClasspathSnapshot.orNull}")
        println("[$path]                            .classpathSnapshot.asPath    '${classpathSnapshotProperties.classpathSnapshot.asPath}'")
        println("[$path]                            .classpath.asPath            '${classpathSnapshotProperties.classpath.asPath}'")
        println("[$path]                            .classpathSnapshotDir.orNull ${classpathSnapshotProperties.classpathSnapshotDir.orNull}")

        println("[$path] compilerOptions.javaParameters ${compilerOptions.javaParameters.orNull}")
        println("[$path]                .jvmTarget      ${compilerOptions.jvmTarget.orNull}")
        println("[$path]                .moduleName     ${compilerOptions.moduleName.orNull}")
        println("[$path]                .noJdk          ${compilerOptions.noJdk.orNull}")

        println("[$path] kotlinJavaToolchainProvider.orNull?.javaVersion?.orNull ${kotlinJavaToolchainProvider.orNull?.javaVersion?.orNull}")

        val refinesEdges = multiplatformStructure.refinesEdges.orNull?.joinToString {
            "RefinesEdge(fromFragmentName:${it.fromFragmentName}, toFragmentName:${it.toFragmentName})"
        }
        println("[$path] multiplatformStructure.refinesEdges '$refinesEdges'")

        val fragments = multiplatformStructure.fragments.orNull?.joinToString {
            "Fragment(fragmentName:${it.fragmentName}, sources:${it.sources.asPath})"
        }
        println("[$path] multiplatformStructure.fragments '$fragments'")
    }
}
