buildscript {
    repositories {
        maven("https://files.minecraftforge.net/maven")
        maven("https://repo.spongepowered.org/maven")
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:3.+")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    `java-library`
}

apply(plugin = "net.minecraftforge.gradle")
apply(plugin = "org.spongepowered.mixin")

group = "pw.dotdash"
version = "1.0-SNAPSHOT"

configure<net.minecraftforge.gradle.userdev.UserDevExtension> {
    mappings("snapshot", "20200709-1.15.1")

    runs {
        create("server") {
            workingDirectory(project.file("./run"))
            args.addAll(listOf("nogui", "--launchTarget", "mukkit"))
            main = "pw.dotdash.mukkit.modlauncher.Main"
        }
    }
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://files.minecraftforge.net/maven")
    maven("https://repo.spongepowered.org/maven")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    "minecraft"("net.minecraft:server:1.15.2")

    implementation("org.bukkit:bukkit:1.15.2-R0.1-SNAPSHOT")

    // CraftBukkit Deps
    implementation("jline:jline:2.12.1")
    implementation("org.ow2.asm:asm:8.0.1")
    implementation("org.jetbrains:annotations:19.0.0")
    implementation("org.apache.logging.log4j:log4j-jul:2.11.2")

    // Mukkit Deps
    implementation("cpw.mods:modlauncher:5.1.+")
    implementation("org.spongepowered:mixin:0.8+")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}