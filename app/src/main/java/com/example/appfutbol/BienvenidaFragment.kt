package com.example.appfutbol

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class BienvenidaFragment : Fragment(R.layout.fragment_bienvenida) {

    private lateinit var btnIniciar: Button
    private lateinit var btnRegistro: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        btnIniciar = view.findViewById(R.id.btnIniciar)
        btnRegistro = view.findViewById(R.id.btnRegistro)

        btnIniciar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        btnRegistro.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegistroFragment())
                .commit()
        }
    }
}