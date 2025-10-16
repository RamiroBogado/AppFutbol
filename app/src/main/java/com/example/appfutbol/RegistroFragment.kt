package com.example.appfutbol

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.edit
import dataBase.AppDatabase
import dataBase.Usuario
import dataBase.UsuarioDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegistroFragment : Fragment(R.layout.fragment_registro) {

    private lateinit var etUsuario: EditText
    private lateinit var etContra: EditText
    private lateinit var etRepetirContra: EditText
    private lateinit var btnContinuar: Button
    private lateinit var btnVolver: Button
    private lateinit var db: AppDatabase
    private lateinit var usuarioDao: UsuarioDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        db = AppDatabase.getDatabase(requireContext())
        usuarioDao = db.usuarioDao()

        setupButtonListener()
    }

    private fun setupButtonListener() {
        btnContinuar.setOnClickListener {
            if (validarCampos()) registrarUsuario()
        }

        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun initViews(view: View) {

        etUsuario = view.findViewById(R.id.etUsuario)
        etContra = view.findViewById(R.id.etContra)
        etRepetirContra = view.findViewById(R.id.etRepetirContra)
        btnContinuar = view.findViewById(R.id.btnContinuar)
        btnVolver = view.findViewById(R.id.btnVolver)
    }

    private fun validarCampos(): Boolean {
        val usuario = etUsuario.text.toString().trim()
        val contra = etContra.text.toString().trim()
        val repetir = etRepetirContra.text.toString().trim()

        if (usuario.isEmpty()) { etUsuario.error = "Ingrese un usuario"; return false }
        if (contra.isEmpty()) { etContra.error = "Ingrese contraseña"; return false }
        if (contra.length < 6) { etContra.error = "Debe tener al menos 6 caracteres"; return false }
        if (repetir.isEmpty()) { etRepetirContra.error = "Repita la contraseña"; return false }
        if (contra != repetir) { etRepetirContra.error = "No coinciden"; return false }

        return true
    }

    private fun registrarUsuario() {
        val usuario = etUsuario.text.toString().trim()
        val contra = etContra.text.toString().trim()

        if (usuario.isEmpty() || contra.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val usuarioExistente = usuarioDao.getByUsuario(usuario)

            withContext(Dispatchers.Main) {
                if (usuarioExistente != null) {
                    Toast.makeText(requireContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show()
                } else {
                    val nuevoUsuario = Usuario(usuario = usuario, pass = contra)

                    CoroutineScope(Dispatchers.IO).launch {
                        usuarioDao.insert(nuevoUsuario)
                    }

                    Toast.makeText(requireContext(), "Usuario $usuario registrado", Toast.LENGTH_SHORT).show()
                    guardarUsuario(usuario)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LoginFragment())
                        .commit()
                }
            }
        }
    }


    private fun guardarUsuario(usuario: String) {
        val prefs = requireContext().getSharedPreferences("AppFutbolPrefs", 0)
        prefs.edit {
            putString("usuario", usuario)
            putBoolean("logueado", true)
        }
    }
}