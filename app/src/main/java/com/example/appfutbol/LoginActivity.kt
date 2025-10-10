package com.example.appfutbol

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    lateinit var etUsuario: EditText
    lateinit var etContra: EditText
    lateinit var cbRecordarUsuario: CheckBox
    lateinit var btnContinuar: Button
    lateinit var btnVolver: Button
    lateinit var sharedPreferences: SharedPreferences

    private val CHANNEL_ID = "canal_recordar_usuario"

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
        crearCanalNotificacion()
        pedirPermisoNotificaciones()
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

        guardarPreferenciasUsuario(usuario, recordarUsuario)

        if (autenticarUsuario(usuario, contra)) {
            Toast.makeText(this, "¡Bienvenido $usuario!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LigasActivity::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun autenticarUsuario(usuario: String, contra: String): Boolean {
        return usuario.isNotEmpty() && contra.length >= 6
    }

    @SuppressLint("MissingPermission")
    private fun guardarPreferenciasUsuario(usuario: String, recordarUsuario: Boolean) {
        sharedPreferences.edit {
            if (recordarUsuario) {
                putString("usuario", usuario)
                mostrarNotificacion()
            } else {
                remove("usuario")
            }
            putBoolean("recordar_usuario", recordarUsuario)
        }
    }

    private fun crearCanalNotificacion() {
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID,
            android.app.NotificationManager.IMPORTANCE_DEFAULT
        ).setName("Recordar Usuario")
            .setDescription("Notificaciones de recordar usuario")
            .build()

        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun mostrarNotificacion() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // asegúrate que sea un icono válido
            .setContentTitle("Usuario recordado")
            .setContentText("Tus datos serán recordados para el próximo inicio de sesión.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
    }
}
