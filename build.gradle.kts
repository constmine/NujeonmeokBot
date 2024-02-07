plugins {
    id("java")
}

group = "io.github.constmine"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        setUrl("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:5.0.0-beta.19")
    implementation("com.sedmelluq:lavaplayer:1.3.77")

}

tasks.test {
    useJUnitPlatform()
}