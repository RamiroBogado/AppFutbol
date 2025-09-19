package com.example.appfutbol

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    lateinit var etUsuario: EditText
    lateinit var etContra: EditText
    lateinit var cbRecordarUsuario: CheckBox
    lateinit var btnContinuar: Button
    lateinit var btnVolver: Button
    lateinit var sharedPreferences: SharedPreferences

    // Base de datos
    lateinit var db: AppDatabase
    lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupSharedPreferences()
        cargarUsuarioGuardado()

        // Inicializar base de datos
        db = AppDatabase.getDatabase(this)
        usuarioDao = db.usuarioDao()

        setupButtonListener()
    }

    private fun initViews() {
        etUsuario = findViewById(R.id.etUsuario)
        etContra = findViewById(R.id.etContra)
        cbRecordarUsuario = findViewById(R.id.cbRecordarUsuario)
        btnContinuar = findViewById(R.id.btnContinuar)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("AppFutbolPrefs", MODE_PRIVATE)
    }

    private fun cargarUsuarioGuardado() {
        val usuarioGuardado = sharedPreferences.getString("usuario", "")
        val recordarUsuario = sharedPreferences.getBoolean("recordar_usuario", false)

        if (recordarUsuario && usuarioGuardado?.isNotEmpty() == true) {
            etUsuario.setText(usuarioGuardado)
            cbRecordarUsuario.isChecked = true
        }
    }

    private fun setupButtonListener() {
        btnContinuar.setOnClickListener {
            if (validarCampos()) {
                iniciarSesion()
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun validarCampos(): Boolean {
        val usuario = etUsuario.text.toString().trim()
        val contra = etContra.text.toString().trim()

        if (usuario.isEmpty()) {
            etUsuario.error = "Por favor ingrese su usuario"
            etUsuario.requestFocus()
            return false
        }

        if (contra.isEmpty()) {
            etContra.error = "Por favor ingrese su contraseña"
            etContra.requestFocus()
            return false
        }

        return true
    }

    private fun iniciarSesion() {
        val usuario = etUsuario.text.toString().trim()
        val contra = etContra.text.toString().trim()
        val recordarUsuario = cbRecordarUsuario.isChecked

        // Guardar preferencias de usuario
        guardarPreferenciasUsuario(usuario, recordarUsuario)

        // Lógica de autenticación
        val usuarioExistente = usuarioDao.getByUsuario(usuario)
        if (usuarioExistente != null && usuarioExistente.pass == contra) {
            Toast.makeText(this, "¡Bienvenido $usuario!", Toast.LENGTH_SHORT).show()

            // Redirigir a LigasActivity
            val intent = Intent(this, LigasActivity::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarPreferenciasUsuario(usuario: String, recordarUsuario: Boolean) {
        sharedPreferences.edit {
            if (recordarUsuario) {
                putString("usuario", usuario)
            } else {
                remove("usuario")
            }
            putBoolean("recordar_usuario", recordarUsuario)
        }
    }
}
