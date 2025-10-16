package com.example.appfutbol

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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
import adapters.PartidoAdapter
import viewmodels.PartidosViewModel
import kotlinx.coroutines.launch

class ProximosPartidosFragment : Fragment() {

    private lateinit var rvPartidos: RecyclerView
    private lateinit var tvTitulo: TextView
    private lateinit var partidoAdapter: PartidoAdapter
    private lateinit var btnVolver: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar

    private val viewModel: PartidosViewModel by viewModels()
    private var currentCompetition: String = "PL"
    private var nombreLiga: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_partidos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentCompetition = arguments?.getString("COMPETITION") ?: "PL"
        nombreLiga = arguments?.getString("NOMBRE")

        initViews(view)
        setupToolbar()
        setupRecyclerView()
        setupButtonVolver()
        setupObservers()
        setupMenuProvider()

        // Cargar próximos partidos
        viewModel.cargarProximosPartidos(currentCompetition)
    }

    private fun initViews(view: View) {
        rvPartidos = view.findViewById(R.id.rvPartidos)
        btnVolver = view.findViewById(R.id.btnVolver)
        toolbar = view.findViewById(R.id.toolbar)
        progressBar = view.findViewById(R.id.progressBar)
        tvTitulo = view.findViewById(R.id.tvTitulo)

        // Configurar título específico
        tvTitulo.text = getString(R.string.titulo_proximosPartidos)
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = nombreLiga

    }

    private fun setupRecyclerView() {
        rvPartidos.layoutManager = LinearLayoutManager(requireContext())
        partidoAdapter = PartidoAdapter(mutableListOf())
        rvPartidos.adapter = partidoAdapter
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.partidosState.collect { state ->
                when (state) {
                    is PartidosViewModel.PartidosState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        rvPartidos.visibility = View.GONE
                    }
                    is PartidosViewModel.PartidosState.Success -> {
                        progressBar.visibility = View.GONE
                        rvPartidos.visibility = View.VISIBLE
                        val partidosConvertidos = viewModel.convertirMatchesAPartidos(state.partidos)
                        partidoAdapter.actualizarPartidos(partidosConvertidos)
                    }
                    is PartidosViewModel.PartidosState.Error -> {
                        progressBar.visibility = View.GONE
                        rvPartidos.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Error: ${state.mensaje}", Toast.LENGTH_LONG).show()
                        partidoAdapter.actualizarPartidos(mutableListOf())
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
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LigasFragment())
                            .commit()
                        true
                    }
                    R.id.item_listado_lista -> {
                        // Volver a ListaFragment
                        parentFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}