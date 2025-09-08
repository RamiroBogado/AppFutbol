package com.example.appfutbol

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit

class RegistroActivity : AppCompatActivity() {
    lateinit var etUsuario: EditText
    lateinit var etContra: EditText
    lateinit var etRepetirContra: EditText
    lateinit var btnContinuar: Button

    lateinit var btnVolver: Button

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
                registrarUsuario()
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

    private fun registrarUsuario() {
        val usuario = etUsuario.text.toString().trim()

        // Mensaje de éxito
        Toast.makeText(this, "Usuario $usuario registrado exitosamente", Toast.LENGTH_SHORT).show()

        //lógica para guardar el usuario en SharedPreferences o base de datos
        guardarUsuario(usuario)

        // Redirigir a la actividad login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun guardarUsuario(usuario: String) {
        // Ejemplo de cómo guardar en SharedPreferences
        val sharedPreferences = getSharedPreferences("AppFutbolPrefs", MODE_PRIVATE)
        sharedPreferences.edit {
            putString("usuario", usuario)
            putBoolean("logueado", true)
        }
    }
}