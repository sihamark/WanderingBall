package eu.heha.samayouwa

import eu.heha.samayouwa.model.PropertiesSettingsDao
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

object IosMain {
    fun initialize() {
        App.initialize(
            App.Requirements(
                settingsDaoFactory = {
                    val folder = Path(iosDirPath("data"))
                    PropertiesSettingsDao(folder)
                }
            )
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    fun iosDirPath(folder:String):String{
        val paths = NSSearchPathForDirectoriesInDomains(
            NSApplicationSupportDirectory,
            NSUserDomainMask,
            true
        )
        val documentsDirectory = paths[0] as String

        val databaseDirectory = "$documentsDirectory/$folder"

        val fileManager = NSFileManager.defaultManager()

        if (!fileManager.fileExistsAtPath(databaseDirectory))
            fileManager.createDirectoryAtPath(databaseDirectory, true, null, null) //Create folder

        return databaseDirectory
    }
}