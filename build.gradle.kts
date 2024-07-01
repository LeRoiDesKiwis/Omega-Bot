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
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testImplementation("org.mockito:mockito-core:5.12.0")
    implementation("net.dv8tion:JDA:5.0.0-beta.24")
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
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

