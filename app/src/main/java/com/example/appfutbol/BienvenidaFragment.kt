package com.example.appfutbol

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class BienvenidaFragment : Fragment(R.layout.fragment_bienvenida) {

    private lateinit var btnIniciar: Button
    private lateinit var btnRegistro: Button

    private lateinit var btnInvitado: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnIniciar = view.findViewById(R.id.btnIniciar)
        btnRegistro = view.findViewById(R.id.btnRegistro)
        btnInvitado = view.findViewById(R.id.btnInvitado)

        btnIniciar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        btnRegistro.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegistroFragment())
                .addToBackStack(null)
                .commit()
        }

        btnInvitado.setOnClickListener {
            val ligasFragment = LigasFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ligasFragment)
                .commit()
        }
    }
}