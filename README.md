# Commercial Trading Checkstyle Plugin

A Gradle plugin that automatically downloads and configures Checkstyle with commercial trading standards.

## Features

- Automatic download of checkstyle configurations from GitHub
- Integration with Gradle's checkstyle plugin
- Configurable via extension DSL
- Support for custom exclude patterns
- Sensible defaults for commercial trading projects

## Usage

### Apply the Plugin

#### Using Kotlin DSL (build.gradle.kts)
```kotlin
plugins {
    id("com.gpo.commercial-trading-checkstyle") version "1.0.0-SNAPSHOT"
}
```

#### Using Groovy DSL (build.gradle)
```groovy
plugins {
    id 'com.gpo.commercial-trading-checkstyle' version '1.0.0-SNAPSHOT'
}
```

### Configuration

The plugin provides an extension for customization:

```kotlin
commercialTradingCheckstyle {
    // Git branch or tag to download configs from (default: "main")
    version.set("main")

    // Directory for checkstyle configs (default: {projectRoot}/config/checkstyle)
    configDir.set(layout.projectDirectory.dir("config/checkstyle"))

    // Auto-download on project evaluation (default: true)
    autoDownload.set(true)

    // Checkstyle version (default: "10.25.0")
    checkstyleVersion.set("10.25.0")

    // Maximum errors (default: 0)
    maxErrors.set(0)

    // Maximum warnings (default: 0)
    maxWarnings.set(0)

    // Additional exclude patterns
    excludePatterns.addAll(
        "**/generated/**",
        "**/proto/**"
    )
}
```

### Tasks

- `downloadCheckstyleConfig` - Manually download checkstyle configuration files
- All standard Checkstyle tasks (automatically configured)

### Example: Multi-Module Project

Root `build.gradle.kts`:
```kotlin
plugins {
    id("com.gpo.commercial-trading-checkstyle") version "1.0.0-SNAPSHOT" apply false
}

subprojects {
    apply(plugin = "com.gpo.commercial-trading-checkstyle")

    commercialTradingCheckstyle {
        excludePatterns.add("**/api/**")
    }
}
```

## Local Development

### Publishing to Maven Local

```bash
./gradlew publishToMavenLocal
```

Then in consuming projects:

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
```

### Development with includeBuild

For testing without publishing:

```kotlin
// settings.gradle.kts in your test project
includeBuild("/path/to/commercial-trading-checkstyle")
```

## Publishing to Nexus

The plugin is automatically published to Nexus via Jenkins when changes are pushed to the repository.

### Jenkins Pipeline

The project uses a Jenkins shared library (`buildGradle`) configured in the `Jenkinsfile`:

- **Gradle version**: 8.14.3 with JDK 17
- **Nexus publishing**: Enabled (automatically publishes releases and snapshots)
- **Release management**: Enabled (handles version tagging)

### Nexus Repositories

- **Snapshots**: `https://nexus.shared.global.com/repository/maven-snapshots/`
- **Releases**: `https://nexus.shared.global.com/repository/maven-releases/`

### Version Management

- SNAPSHOT versions are published to the snapshots repository
- Release versions are published to the releases repository
- Jenkins automatically handles versioning based on git tags

### Using from Nexus

To use the plugin from Nexus in your projects:

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        maven {
            url = uri("https://nexus.shared.global.com/repository/maven/")
        }
        gradlePluginPortal()
    }
}
```

Then apply the plugin:

```kotlin
plugins {
    id("com.gpo.commercial-trading-checkstyle") version "1.0.0"
}
```

### Manual Publishing to Nexus

If you need to manually publish to Nexus (requires credentials):

```bash
./gradlew publish -PnexusUsername=<username> -PnexusPassword=<password>
```

Or set environment variables:

```bash
export NEXUS_USERNAME=<username>
export NEXUS_PASSWORD=<password>
./gradlew publish
```

## Migration from Manual Configuration

### Before (gplan-pack style)
```groovy
tasks.register('getCheckStyleConfig') {
    group 'Sample category'
    def checkstyleConfigPath = "${project.rootDir}/config/checkstyle"
    var checkStyleUrl = "https://raw.githubusercontent.com/..."
    var exclusionsUrl = "https://raw.githubusercontent.com/..."

    mkdir checkstyleConfigPath
    ant.get(src: checkStyleUrl, dest: checkstyleConfigPath+'/checkstyle.xml')
    ant.get(src: exclusionsUrl, dest: checkstyleConfigPath+'/suppressions.xml')
}

tasks.withType(Checkstyle).configureEach {
    exclude '**/gpo/api/**'
    mustRunAfter 'getCheckStyleConfig'
}
```

### After (using plugin)
```kotlin
plugins {
    id("com.gpo.commercial-trading-checkstyle") version "1.0.0-SNAPSHOT"
}

commercialTradingCheckstyle {
    excludePatterns.add("**/gpo/api/**")
}
```

## Files

- `checkstyle/checkstyle.xml` - Main checkstyle configuration (Google Style Guide based)
- `checkstyle/suppressions.xml` - Suppression rules for test files and generated code
- `java-code-formatter-intellij.xml` - IntelliJ IDEA formatter configuration

## Configuration Details

The plugin downloads checkstyle configurations from this repository's `main` branch. The checkstyle rules include:

- Google Java Style Guide compliance
- Line length: 140 characters
- Indentation: 4 spaces
- Comprehensive naming conventions
- Import ordering
- Javadoc requirements
- Code quality checks

## License

Internal use only - Global Engineering Shared
