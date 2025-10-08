package com.example.appfutbol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.edit

class RegistroFragment : Fragment(R.layout.fragment_registro) {

    private lateinit var etUsuario: EditText
    private lateinit var etContra: EditText
    private lateinit var etRepetirContra: EditText
    private lateinit var btnContinuar: Button
    private lateinit var btnVolver: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etUsuario = view.findViewById(R.id.etUsuario)
        etContra = view.findViewById(R.id.etContra)
        etRepetirContra = view.findViewById(R.id.etRepetirContra)
        btnContinuar = view.findViewById(R.id.btnContinuar)
        btnVolver = view.findViewById(R.id.btnVolver)

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
        Toast.makeText(requireContext(), "Usuario $usuario registrado", Toast.LENGTH_SHORT).show()
        guardarUsuario(usuario)

        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun guardarUsuario(usuario: String) {
        val prefs = requireContext().getSharedPreferences("AppFutbolPrefs", 0)
        prefs.edit {
            putString("usuario", usuario)
            putBoolean("logueado", true)
        }
    }
}
