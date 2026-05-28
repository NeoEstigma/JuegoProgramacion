plugins {
    id 'application'
    id 'org.openjfx.javafxplugin' version '0.1.0'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.mongodb:mongodb-driver-sync:5.2.1'
}

javafx {
    version = '21.0.2'
    modules = [ 'javafx.controls', 'javafx.fxml' ]
}

application {
    mainClass = 'application.Main'
}