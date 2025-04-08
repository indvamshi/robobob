import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.12.0"
}

group = "com.test"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.15")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.graalvm.sdk:graal-sdk:24.1.0")
	implementation("org.graalvm.js:js:24.1.0")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.4.4")

	compileOnly("org.projectlombok:lombok:1.18.38")
	annotationProcessor("org.projectlombok:lombok:1.18.38")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

	testCompileOnly("org.projectlombok:lombok:1.18.38")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.named<GenerateTask>("openApiGenerate") {
	inputSpec.set("$rootDir/src/main/resources/spec/robobob-api-spec.yaml")
	generatorName.set("spring")
	apiPackage.set("com.maths.challenge.generated.api")
	modelPackage.set("com.maths.challenge.generated.model")
	outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)

	configOptions.set(
		mapOf(
			"interfaceOnly" to "true",
			"useTags" to "true",
			"modelNameMappings" to "CreateQuestionsRequest:QuestionRequest",
			"dateLibrary" to "java8",
			"library" to "spring-boot",                 // Use the spring-boot generator (includes Jakarta support)
			"useSpringBoot3" to "true",
			"jakarta" to "true",
			"useBeanValidation" to "true"
		)
	)
}

// Delete generated code on clean
tasks.named<Delete>("clean") {
	delete(file(layout.buildDirectory.dir("generated")))
}

// Include the generated sources in the Java source set
sourceSets["main"].java {
	srcDir(layout.buildDirectory.dir("generated/src/main/java"))
}

// Ensure generate task runs before compile
tasks.named("compileJava") {
	dependsOn("openApiGenerate")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

