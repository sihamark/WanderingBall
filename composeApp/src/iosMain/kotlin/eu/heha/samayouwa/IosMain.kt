package eu.heha.samayouwa

import kotlinx.io.files.Path
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

object IosMain {
    fun initialize() {
        App.initialize(
            App.Requirements(
                rootFolder = Path(
                    iosDirPath("data")
                )
            )
        )
    }

    @OptIn(UnsafeNumber::class)
    fun iosDirPath(folder:String):String{
        val paths = NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, true);
        val documentsDirectory = paths[0] as String;

        val databaseDirectory = "$documentsDirectory/$folder"

        val fileManager = NSFileManager.defaultManager()

        if (!fileManager.fileExistsAtPath(databaseDirectory))
            fileManager.createDirectoryAtPath(databaseDirectory, true, null, null); //Create folder

        return databaseDirectory
    }
}