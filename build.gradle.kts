import org.gradle.api.JavaVersion.VERSION_1_8

project.description = "A fast, lightweight Scoreboard for your server."
project.version = "1.5.0"

plugins {
    id("java-library")
}

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { setUrl("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { setUrl("https://jitpack.io/") }
}

dependencies {
    val lombokVersion = "1.18.10"
    val mockBukkitVersion = "1.16:0.5.0"
    val placeholderApiVersion = "2.10.3"
    val spigotVersion = "1.13.2-R0.1-SNAPSHOT"

    api("org.spigotmc:spigot-api:$spigotVersion")
    api("me.clip:placeholderapi:$placeholderApiVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation("org.spigotmc:spigot-api:$spigotVersion")
    testImplementation("me.clip:placeholderapi:$placeholderApiVersion")
    testImplementation("com.github.seeseemelk:MockBukkit-v$mockBukkitVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}