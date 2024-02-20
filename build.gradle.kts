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

    implementation("org.jsoup:jsoup:1.13.1")

    implementation("com.google.api-client:google-api-client:1.32.2")
    implementation("com.google.http-client:google-http-client-jackson2:1.43.2")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20240213-2.0.0")

}

tasks.test {
    useJUnitPlatform()
}