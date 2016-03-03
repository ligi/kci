package kci

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

val outPath = File("output")

fun main(args: Array<String>) {

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
}

