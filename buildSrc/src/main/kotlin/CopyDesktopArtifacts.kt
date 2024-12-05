import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.support.zipTo
import java.io.File
import javax.inject.Inject

abstract class CopyDesktopArtifacts @Inject constructor(
    @Inject private val layout: ProjectLayout
) : DefaultTask() {

    @get:Input
    abstract val intoFolder: Property<File>

    @get:Input
    abstract val artefactName: Property<String>

    @TaskAction
    fun action() {
        val build = layout.buildDirectory.get().dir("compose/binaries/main-release/app")
        intoFolder.get().mkdirs()
        val osSlug = currentOS.id
        zipTo(intoFolder.get().resolve("${artefactName.get()}.$osSlug.zip"), build.asFile)
    }

    enum class OS(val id: String) {
        Linux("linux"),
        Windows("windows"),
        MacOS("macos")
    }

    private val currentOS: OS
        get() {
            val os = System.getProperty("os.name")
            return when {
                os.equals("Mac OS X", ignoreCase = true) -> OS.MacOS
                os.startsWith("Win", ignoreCase = true) -> OS.Windows
                os.startsWith("Linux", ignoreCase = true) -> OS.Linux
                else -> error("Unknown OS name: $os")
            }
        }
}