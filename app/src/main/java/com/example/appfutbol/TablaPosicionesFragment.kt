package com.example.appfutbol

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapters.TablaPosicionesAdapter
import viewmodels.TablaPosicionesViewModel
import kotlinx.coroutines.launch

class TablaPosicionesFragment : Fragment() {

    private lateinit var rvTablaPosiciones: RecyclerView
    private lateinit var tablaPosicionesAdapter: TablaPosicionesAdapter
    private lateinit var btnVolver: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar

    private val viewModel: TablaPosicionesViewModel by viewModels()
    private var currentCompetition: String = "PL"
    private var nombreLiga: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tabla_posiciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recibir argumentos
        currentCompetition = arguments?.getString("COMPETITION") ?: "PL"
        nombreLiga = arguments?.getString("NOMBRE")

        initViews(view)
        setupToolbar()
        setupRecyclerView()
        setupButtonVolver()
        setupObservers()
        setupMenuProvider()

        // Cargar datos
        viewModel.cargarTablaPosiciones(currentCompetition)
    }

    private fun initViews(view: View) {
        rvTablaPosiciones = view.findViewById(R.id.rvTablaPosiciones)
        btnVolver = view.findViewById(R.id.btnVolver)
        toolbar = view.findViewById(R.id.toolbar)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = nombreLiga
    }

    private fun setupRecyclerView() {
        rvTablaPosiciones.layoutManager = LinearLayoutManager(requireContext())
        tablaPosicionesAdapter = TablaPosicionesAdapter(mutableListOf())
        rvTablaPosiciones.adapter = tablaPosicionesAdapter
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.tablaPosicionesState.collect { state ->
                when (state) {
                    is TablaPosicionesViewModel.TablaPosicionesState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        rvTablaPosiciones.visibility = View.GONE
                    }
                    is TablaPosicionesViewModel.TablaPosicionesState.Success -> {
                        progressBar.visibility = View.GONE
                        rvTablaPosiciones.visibility = View.VISIBLE
                        tablaPosicionesAdapter.actualizarEquipos(state.equipos)
                    }
                    is TablaPosicionesViewModel.TablaPosicionesState.Error -> {
                        progressBar.visibility = View.GONE
                        rvTablaPosiciones.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Error: ${state.mensaje}", Toast.LENGTH_LONG).show()
                        tablaPosicionesAdapter.actualizarEquipos(mutableListOf())
                    }
                }
            }
        }
    }

    private fun setupMenuProvider() {

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
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
                    R.id.item_listado_lista -> {
                        // Navegar de vuelta a ListaFragment
                        requireActivity().supportFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}