package com.example.appfutbol

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName ="Usuarios")
data class Usuario(
    @ColumnInfo(name = "usuario") val usuario: String,
    @ColumnInfo(name = "pass") val pass: String
){
    @PrimaryKey(autoGenerate = true)val id: Int=0
}
