rootProject.name = "megalodonte-components"

// Build against the current ecosystem sources; published consumers can opt out.
if (providers.gradleProperty("useLocalSources").orNull != "false") {
    listOf("core", "base").forEach { name ->
        val dependency = file("../megalodonte-$name")
        if (dependency.isDirectory) includeBuild(dependency)
    }
}
