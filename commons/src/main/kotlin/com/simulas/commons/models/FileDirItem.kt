package com.simulas.commons.models

import android.content.Context
import com.simulas.commons.extensions.*
import com.simulas.commons.helpers.*
import java.io.File

open class FileDirItem(val path: String, val name: String = "", var isDirectory: Boolean = false, var children: Int = 0, var size: Long = 0L, var modified: Long = 0L) :
        Comparable<FileDirItem> {
    companion object {
        var sorting = 0
    }

    override fun toString() = "FileDirItem(path=$path, name=$name, isDirectory=$isDirectory, children=$children, size=$size, modified=$modified)"

    override fun compareTo(other: FileDirItem): Int {
        return if (isDirectory && !other.isDirectory) {
            -1
        } else if (!isDirectory && other.isDirectory) {
            1
        } else {
            var result: Int
            when {
                sorting and SORT_BY_NAME != 0 -> result = name.toLowerCase().compareTo(other.name.toLowerCase())
                sorting and SORT_BY_SIZE != 0 -> result = when {
                    size == other.size -> 0
                    size > other.size -> 1
                    else -> -1
                }
                sorting and SORT_BY_DATE_MODIFIED != 0 -> {
                    result = when {
                        modified == other.modified -> 0
                        modified > other.modified -> 1
                        else -> -1
                    }
                }
                else -> {
                    result = getExtension().toLowerCase().compareTo(other.getExtension().toLowerCase())
                }
            }

            if (sorting and SORT_DESCENDING != 0) {
                result *= -1
            }
            result
        }
    }

    fun getExtension() = if (isDirectory) name else path.substringAfterLast('.', "")

    fun getBubbleText(context: Context) = when {
        sorting and SORT_BY_SIZE != 0 -> size.formatSize()
        sorting and SORT_BY_DATE_MODIFIED != 0 -> modified.formatDate(context)
        sorting and SORT_BY_EXTENSION != 0 -> getExtension().toLowerCase()
        else -> name
    }

    fun getProperSize(countHidden: Boolean) = File(path).getProperSize(countHidden)

    fun getProperFileCount(countHidden: Boolean) = File(path).getFileCount(countHidden)

    fun getDirectChildrenCount(countHiddenItems: Boolean) = File(path).getDirectChildrenCount(countHiddenItems)

    fun getParentPath() = path.getParentPath()

    fun getDuration() = path.getDuration()

    fun getFileDurationSeconds() = path.getFileDurationSeconds()

    fun getArtist() = path.getFileArtist()

    fun getAlbum() = path.getFileAlbum()

    fun getSongTitle() = path.getFileSongTitle()

    fun getResolution(context: Context) = context.getResolution(path)

    fun getVideoResolution(context: Context) = context.getVideoResolution(path)

    fun getImageResolution() = path.getImageResolution()
}
