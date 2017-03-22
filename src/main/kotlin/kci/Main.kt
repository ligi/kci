package kci


import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.netty.embeddedNettyServer
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.routing
import java.io.File

val outPath = File("output")

fun main(args: Array<String>) {
    embeddedNettyServer(9001) {
        routing {
            post ("/") {

                val get = call.request.content[String::class]
                println(""+ get)
                call.respondText("Thanks GitHub!", ContentType.Text.Plain)
            }
        }
    }.start(wait = true)
    /*
    
    outPath.mkdir()
    while (true) {
        File("workspace").listFiles().forEach { projectFile ->

            val repo = FileRepositoryBuilder().findGitDir(projectFile).build()

            Git(repo).fetch().call().messages.forEach {
                println(it)
            }

            val projectPath = File(outPath, projectFile.name)
            projectPath.mkdir()

            repo.tags.forEach { tag ->

                val tagPath = File(projectPath, tag.key)

                if (!tagPath.exists()) {
                    tagPath.mkdir()
                    println("processing ${projectFile.name} ${tag.key} ")

                    executeAndPrint("git", "checkout", tag.key, outPath = tagPath, workPath = projectFile)
                    executeAndPrint("./gradlew", "clean", "assembleRelease", outPath = tagPath, workPath = projectFile)

                    val apkPath = File(tagPath, "apkpath")
                    apkPath.mkdir()
                    projectFile.walk().filter { it.name.endsWith(".apk") }.forEach { it.copyTo(File(apkPath, it.name), true) }

                    executeAndPrint("./gradlew", "clean", outPath = tagPath, workPath = projectFile)

                    println("tag done")
                }

            }


        }

        Thread.sleep(3000)
    }
*/
}

