package dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="Usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)val id: Int=0,
    @ColumnInfo(name = "usuario") val usuario: String,
    @ColumnInfo(name = "pass") val pass: String
)