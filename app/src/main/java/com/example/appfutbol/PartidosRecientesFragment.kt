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
import adapters.PartidoAdapter
import androidx.fragment.app.FragmentManager
import viewmodels.PartidosViewModel
import kotlinx.coroutines.launch

class PartidosRecientesFragment : Fragment() {

    private lateinit var rvPartidos: RecyclerView
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
        viewModel.cargarPartidosRecientes(currentCompetition)
    }

    private fun initViews(view: View) {
        rvPartidos = view.findViewById(R.id.rvPartidos)
        btnVolver = view.findViewById(R.id.btnVolver)
        toolbar = view.findViewById(R.id.toolbar)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = nombreLiga
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        rvPartidos.layoutManager = layoutManager
        partidoAdapter = PartidoAdapter(mutableListOf())
        rvPartidos.adapter = partidoAdapter
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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
                        requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, BienvenidaFragment())
                            .disallowAddToBackStack()
                            .commit()
                        true
                    }
                    R.id.item_listado_ligas -> {
                        // Navegar a LigasFragment
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LigasFragment())
                            .disallowAddToBackStack()
                            .commit()
                        true
                    }
                    R.id.item_listado_lista -> {
                        // Navegar de vuelta a ListaFragment
                        parentFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}