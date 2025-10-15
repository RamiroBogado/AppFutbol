package com.example.appfutbol

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

class ListaFragment : Fragment() {

    private lateinit var btnPartidosRecientes: LinearLayout
    private lateinit var btnProximosPartidos: LinearLayout
    private lateinit var btnTabla: LinearLayout
    private lateinit var btnGoleadores: LinearLayout
    private lateinit var btnVolver: Button
    private lateinit var toolbar: Toolbar

    private var currentCompetition: String = "PL"
    private var nombreLiga: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recibir los argumentos
        currentCompetition = arguments?.getString("COMPETITION") ?: "PL"
        nombreLiga = arguments?.getString("NOMBRE")

        initViews(view)
        setupToolbar(view)
        setupButtonListeners()
        setupMenuProvider()
    }

    private fun initViews(view: View) {
        btnPartidosRecientes = view.findViewById(R.id.btnPartidosRecientes)
        btnProximosPartidos = view.findViewById(R.id.btnProximosPartidos)
        btnTabla = view.findViewById(R.id.btnTabla)
        btnGoleadores = view.findViewById(R.id.btnGoleadores)
        btnVolver = view.findViewById(R.id.btnVolver)
    }

    private fun setupToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = nombreLiga

    }

    private fun setupButtonListeners() {
        btnPartidosRecientes.setOnClickListener {
            navigateToPartidosRecientes()
        }

        btnProximosPartidos.setOnClickListener {
            navigateToProximosPartidos()
        }

        btnTabla.setOnClickListener {
            navigateToTablaPosiciones()
        }

        btnGoleadores.setOnClickListener {
            navigateToGoleadores()
        }

        btnVolver.setOnClickListener {
            // Volver al fragment anterior
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun navigateToPartidosRecientes() {
        val fragment = PartidosRecientesFragment().apply {
            arguments = Bundle().apply {
                putString("COMPETITION", currentCompetition)
                putString("NOMBRE", nombreLiga)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("lista")
            .commit()
    }

    private fun navigateToProximosPartidos() {
        val fragment = ProximosPartidosFragment().apply {
            arguments = Bundle().apply {
                putString("COMPETITION", currentCompetition)
                putString("NOMBRE", nombreLiga)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("lista")
            .commit()
    }

    private fun navigateToTablaPosiciones() {
        val fragment = TablaPosicionesFragment().apply {
            arguments = Bundle().apply {
                putString("COMPETITION", currentCompetition)
                putString("NOMBRE", nombreLiga)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("lista")
            .commit()
    }

    private fun navigateToGoleadores() {
        val fragment = GoleadoresFragment().apply {
            arguments = Bundle().apply {
                putString("COMPETITION", currentCompetition)
                putString("NOMBRE", nombreLiga)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("lista")
            .commit()
    }

    private fun setupMenuProvider() {

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)

                // Ocultar ítem específico para ListaFragment
                val listadoItem = menu.findItem(R.id.item_listado_lista)
                listadoItem?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_logout -> {
                        requireActivity().finish()
                        true
                    }
                    R.id.item_listado_ligas -> {
                        // Navegar a LigasFragment
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LigasFragment())
                            .commit()
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}