package com.nickmorus.pregnancyapp.data.db

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object DatabaseHelper {
    fun copyDatabaseFromAssets(context: Context) {
        val databaseFile = File(context.getDatabasePath("app.db").path)
        if (!databaseFile.exists()) {
            context.assets.open("data_pregnancy.db").use { inputStream ->
                FileOutputStream(databaseFile).use { outputStream ->
                    copyStream(inputStream, outputStream)
                }
            }
        }
    }
    private fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
    }
}
