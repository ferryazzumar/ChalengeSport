package com.example.chalengesport.commons.utils

import com.example.chalengesport.commons.utils.Utils.Companion.string
import java.io.File

class FileUtils {
    companion object{

        fun String?.fileOf(): File? {
            return if (containWhiteSpace()) null else File(this.string())
        }

        private fun String?.containWhiteSpace(): Boolean {
            if (this == null) return true
            var character = 0
            while (character < this.length) {
                if (!Character.isWhitespace(this[character])) {
                    return false
                }
                ++character
            }
            return true
        }
    }
}