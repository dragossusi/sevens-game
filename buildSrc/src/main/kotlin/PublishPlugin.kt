import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

import org.gradle.plugins.signing.SigningPlugin

/**
 * SevensGame
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * SevensGame is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * SevensGame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SevensGame.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.run {
        apply<MavenPublishPlugin>()
        apply<SigningPlugin>()
        version = Versions.app
        group = Details.groupId
        publishing {
            publications {
                publications.withType<MavenPublication> {
                    artifactId = target.name
                    version = Versions.app
                    group = Details.groupId
                    pom {
                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        developers {
                            developer {
                                id.set("dragossusi")
                                name.set("Dragos Rachieru")
                                email.set("rachierudragos97@gmail.com")
                            }
                        }
                        scm {
                            connection.set("scm:git:git://github.com/dragossusi/sevens-game.git")
                            developerConnection.set("scm:git:ssh://github.com/dragossusi/sevens-game.git")
                            url.set("https://github.com/dragossusi/sevens-core/")
                        }
                    }
                }
            }
            repositories {
                maven {
                    name = "sonatype"
                    url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = project.property("sonatype.username").toString()
                        password = project.property("sonatype.password").toString()
                    }
                }
            }
        }
        signing {
            sign(publishing.publications)
        }
    }
}