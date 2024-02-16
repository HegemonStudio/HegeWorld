plugins {
    `java-library`
    `maven-publish`
}

group = "com.hegemonstudio"
version = "0.1-PRE"
description = "HegeWorld"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api(libs.io.papermc.paper.paper.api)

    compileOnly(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.projectlombok.lombok)

    api(libs.com.sk89q.worldedit.worldedit.bukkit)
    api(libs.com.github.moderrek.impactmc)

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.2")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}