plugins {
    id("java")
}

group = "fr.leroideskiwis.omegabot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:5.0.0-beta.24")
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
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