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
import adapters.GoleadoresAdapter
import androidx.fragment.app.FragmentManager
import viewmodels.GoleadoresViewModel
import kotlinx.coroutines.launch

class GoleadoresFragment : Fragment() {

    private lateinit var rvGoleadores: RecyclerView
    private lateinit var goleadoresAdapter: GoleadoresAdapter
    private lateinit var btnVolver: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    private val viewModel: GoleadoresViewModel by viewModels()
    private var currentCompetition: String = "PL"
    private var nombreLiga: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goleadores, container, false)
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
        viewModel.cargarGoleadores(currentCompetition)
    }

    private fun initViews(view: View) {
        rvGoleadores = view.findViewById(R.id.rvGoleadores)
        btnVolver = view.findViewById(R.id.btnVolver)
        toolbar = view.findViewById(R.id.toolbar)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = nombreLiga
    }

    private fun setupRecyclerView() {
        rvGoleadores.layoutManager = LinearLayoutManager(requireContext())
        goleadoresAdapter = GoleadoresAdapter(mutableListOf()) { playerId ->
            navigateToPlayerDetail(playerId)
        }
        rvGoleadores.adapter = goleadoresAdapter
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.goleadoresState.collect { state ->
                when (state) {
                    is GoleadoresViewModel.GoleadoresState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        rvGoleadores.visibility = View.GONE
                    }
                    is GoleadoresViewModel.GoleadoresState.Success -> {
                        progressBar.visibility = View.GONE
                        rvGoleadores.visibility = View.VISIBLE
                        goleadoresAdapter.actualizarGoleadores(state.goleadores)
                    }
                    is GoleadoresViewModel.GoleadoresState.Error -> {
                        progressBar.visibility = View.GONE
                        rvGoleadores.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Error: ${state.mensaje}", Toast.LENGTH_LONG).show()
                        goleadoresAdapter.actualizarGoleadores(mutableListOf())
                    }
                }
            }
        }
    }

    private fun navigateToPlayerDetail(playerId: Int) {
        val playerDetailFragment = PlayerDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("PLAYER_ID", playerId)
                putString("COMPETITION", currentCompetition)
                putString("NOMBRE", nombreLiga)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, playerDetailFragment)
            .addToBackStack("goleadores")
            .commit()
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
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LigasFragment())
                            .disallowAddToBackStack()
                            .commit()
                        true
                    }
                    R.id.item_listado_lista -> {
                        requireActivity().supportFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}