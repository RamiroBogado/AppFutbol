package com.example.appfutbol

import androidx.room.*

@Dao
interface UsuarioDao {

    // Insertar un usuario
    @Insert
    suspend fun insert(usuario: Usuario)

    // Obtener un usuario por nombre de usuario (para comprobar existencia)
    @Query("SELECT * FROM usuarios WHERE usuario = :nombreUsuario LIMIT 1")
    suspend fun getByUsuario(nombreUsuario: String): Usuario?

    // Verificar usuario y contraseña
    @Query("SELECT * FROM usuarios WHERE usuario = :nombreUsuario AND pass = :contrasena LIMIT 1")
    suspend fun login(nombreUsuario: String, contrasena: String): Usuario?
}
