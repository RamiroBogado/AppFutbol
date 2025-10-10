package com.example.appfutbol

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment

class LoginFragment : Fragment(R.layout.fragment_login) {
        lateinit var etUsuario: EditText
        lateinit var etContra: EditText
        lateinit var cbRecordarUsuario: CheckBox
        lateinit var btnContinuar: Button
        lateinit var btnVolver: Button
        lateinit var sharedPreferences: SharedPreferences
        val CHANNEL_ID = "canal_recordar_usuario"

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Inicializar views
            etUsuario = view.findViewById(R.id.etUsuario)
            etContra = view.findViewById(R.id.etContra)
            cbRecordarUsuario = view.findViewById(R.id.cbRecordarUsuario)
            btnContinuar = view.findViewById(R.id.btnContinuar)
            btnVolver = view.findViewById(R.id.btnVolver)

            // SharedPreferences
            sharedPreferences = requireContext().getSharedPreferences("AppFutbolPrefs", AppCompatActivity.MODE_PRIVATE)

            // Notificaciones
            crearCanalNotificacion()
            pedirPermisoNotificaciones()

            // Cargar usuario guardado
            cargarUsuarioGuardado()

            // Listeners
            setupButtonListener()
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
                parentFragmentManager.popBackStack() // vuelve al fragmento anterior
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
                Toast.makeText(requireContext(), "¡Bienvenido $usuario!", Toast.LENGTH_SHORT).show()

                // Si querés abrir otra activity (por ejemplo LigasActivity), usá Intent normal:
                val intent = Intent(requireContext(), LigasActivity::class.java)
                intent.putExtra("usuario", usuario)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
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

            NotificationManagerCompat.from(requireContext()).createNotificationChannel(channel)
        }

        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        private fun mostrarNotificacion() {
            val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Usuario recordado")
                .setContentText("Tus datos serán recordados para el próximo inicio de sesión.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            NotificationManagerCompat.from(requireContext()).notify(1, notification)
        }

        private fun pedirPermisoNotificaciones() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
}