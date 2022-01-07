package com.niku.moneymate.files

import java.io.File

class FileOperations {

    fun readFileLineByLine(fileName: String)
        = File(fileName).forEachLine { println(it) }

}