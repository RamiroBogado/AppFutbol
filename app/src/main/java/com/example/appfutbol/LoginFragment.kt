package com.example.appfutbol

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import dataBase.AppDatabase
import dataBase.UsuarioDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var etUsuario: EditText
    private lateinit var etContra: EditText
    private lateinit var cbRecordarUsuario: CheckBox
    private lateinit var btnContinuar: Button
    private lateinit var btnVolver: Button
    private lateinit var sharedPreferences: SharedPreferences

    // Base de datos
    private lateinit var db: AppDatabase
    private lateinit var usuarioDao: UsuarioDao

    private val cannelId = "canal_recordar_usuario"

    // Nuevo sistema de permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido
            Toast.makeText(requireContext(), "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
        } else {
            // Permiso denegado
            Toast.makeText(requireContext(), "Los permisos de notificación son necesarios", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar views
        initViews(view)

        // SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("AppFutbolPrefs", AppCompatActivity.MODE_PRIVATE)

        // Inicializar base de datos
        db = AppDatabase.getDatabase(requireContext())
        usuarioDao = db.usuarioDao()

        // Notificaciones
        crearCanalNotificacion()
        pedirPermisoNotificaciones()

        // Cargar usuario guardado
        cargarUsuarioGuardado()

        // Listeners
        setupButtonListener()
    }

    private fun initViews(view: View) {

        etUsuario = view.findViewById(R.id.etUsuario)
        etContra = view.findViewById(R.id.etContra)
        cbRecordarUsuario = view.findViewById(R.id.cbRecordarUsuario)
        btnContinuar = view.findViewById(R.id.btnContinuar)
        btnVolver = view.findViewById(R.id.btnVolver)
    }

    private fun cargarUsuarioGuardado() {
        val usuarioGuardado = sharedPreferences.getString("usuario", "")
        val recordarUsuario = sharedPreferences.getBoolean("recordar_usuario", false)

        if (recordarUsuario && !usuarioGuardado.isNullOrEmpty()) {
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
            parentFragmentManager.popBackStack()
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

        // Usar corrutinas para la operación de base de datos
        CoroutineScope(Dispatchers.Main).launch {
            val usuarioExistente = usuarioDao.getByUsuario(usuario)

            if (usuarioExistente != null && usuarioExistente.pass == contra) {
                Toast.makeText(requireContext(), "¡Bienvenido $usuario!", Toast.LENGTH_SHORT).show()

                // Navegar a LigasFragment
                val ligasFragment = LigasFragment().apply {
                    arguments = Bundle().apply {
                        putString("usuario", usuario)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ligasFragment)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun guardarPreferenciasUsuario(usuario: String, recordarUsuario: Boolean) {
        sharedPreferences.edit {
            if (recordarUsuario) {
                putString("usuario", usuario)
                // Solo mostrar notificación si tenemos permiso
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
                    mostrarNotificacion()
                }
            } else {
                remove("usuario")
            }
            putBoolean("recordar_usuario", recordarUsuario)
        }
    }

    private fun crearCanalNotificacion() {
        val channel = NotificationChannelCompat.Builder(
            cannelId,
            android.app.NotificationManager.IMPORTANCE_DEFAULT
        ).setName("Recordar Usuario")
            .setDescription("Notificaciones de recordar usuario")
            .build()

        NotificationManagerCompat.from(requireContext()).createNotificationChannel(channel)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun mostrarNotificacion() {
        val notification = NotificationCompat.Builder(requireContext(), cannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Usuario recordado")
            .setContentText("Tus datos serán recordados para el próximo inicio de sesión.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(requireContext()).notify(1, notification)
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Usar el nuevo sistema de permisos
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        // Para versiones anteriores a Android 13, no se necesita permiso explícito
    }
}