plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "fr.leroideskiwis.omegabot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.2")
    testImplementation("org.mockito:mockito-core:5.14.1")
    implementation("net.dv8tion:JDA:5.1.2")
    implementation("org.xerial:sqlite-jdbc:3.46.1.3")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")

}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("run") {
    group = "application"
    description = "Runs the main class"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("fr.leroideskiwis.omegabot.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "fr.leroideskiwis.omegabot.Main" // Remplacez par votre classe principale
    }
}

