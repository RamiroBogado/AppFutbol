package dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {

    // Insertar un usuario
    @Insert
    fun insert(usuario: Usuario)

    // Obtener un usuario por nombre de usuario (para comprobar existencia)
    @Query("SELECT * FROM Usuarios WHERE usuario = :nombreUsuario LIMIT 1")
    fun getByUsuario(nombreUsuario: String): Usuario?

    // Verificar usuario y contraseña
    @Query("SELECT * FROM Usuarios WHERE usuario = :nombreUsuario AND pass = :contrasena LIMIT 1")
    fun login(nombreUsuario: String, contrasena: String): Usuario?
}