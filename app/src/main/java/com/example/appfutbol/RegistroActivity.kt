package com.example.appfutbol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroActivity : AppCompatActivity() {

    lateinit var etUsuario: EditText
    lateinit var etContra: EditText
    lateinit var etRepetirContra: EditText
    lateinit var btnContinuar: Button
    lateinit var btnVolver: Button

    lateinit var db: AppDatabase
    lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        // Inicializar base de datos
        db = AppDatabase.getDatabase(this)
        usuarioDao = db.usuarioDao()

        setupButtonListener()
    }

    private fun initViews() {
        etUsuario = findViewById(R.id.etUsuario)
        etContra = findViewById(R.id.etContra)
        etRepetirContra = findViewById(R.id.etRepetirContra)
        btnContinuar = findViewById(R.id.btnContinuar)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupButtonListener() {
        btnContinuar.setOnClickListener {
            if (validarCampos()) {
                registrarUsuarioRoom()
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun validarCampos(): Boolean {
        val usuario = etUsuario.text.toString().trim()
        val contra = etContra.text.toString().trim()
        val repetirContra = etRepetirContra.text.toString().trim()

        if (usuario.isEmpty()) {
            etUsuario.error = "Por favor ingrese un usuario"
            etUsuario.requestFocus()
            return false
        }

        if (contra.isEmpty()) {
            etContra.error = "Por favor ingrese una contraseña"
            etContra.requestFocus()
            return false
        }

        if (contra.length < 6) {
            etContra.error = "La contraseña debe tener al menos 6 caracteres"
            etContra.requestFocus()
            return false
        }

        if (repetirContra.isEmpty()) {
            etRepetirContra.error = "Por favor repita la contraseña"
            etRepetirContra.requestFocus()
            return false
        }

        if (contra != repetirContra) {
            etRepetirContra.error = "Las contraseñas no coinciden"
            etRepetirContra.requestFocus()
            return false
        }

        return true
    }

    // Registrar usuario en base de datos
    private fun registrarUsuarioRoom() {
        val nombreUsuario = etUsuario.text.toString().trim()
        val contrasena = etContra.text.toString().trim()

        val existente = usuarioDao.getByUsuario(nombreUsuario)
        if (existente != null) {
            Toast.makeText(this, "Usuario ya existe", Toast.LENGTH_SHORT).show()
        } else {
            usuarioDao.insert(Usuario(usuario = nombreUsuario, pass = contrasena))
            Toast.makeText(this, "Usuario $nombreUsuario registrado exitosamente", Toast.LENGTH_SHORT).show()

            // Redirigir a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
