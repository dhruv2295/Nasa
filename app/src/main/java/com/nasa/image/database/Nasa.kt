package com.nasa.image.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nasa")
class Nasa(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val date: String?,
    val explanation: String?,
    val hdurl: String?,
    val media_type: String?,
    val service_version: String?,
    val title: String?,
    val url: String?
)