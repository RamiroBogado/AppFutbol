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
import dtos.TeamDetailDTO
import kotlinx.coroutines.launch
import viewmodels.TeamDetailViewModel

class TeamDetailFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var btnVolver: Button

    // Views para mostrar los datos del equipo
    private lateinit var tvNombre: TextView
    private lateinit var tvNombreCorto: TextView
    private lateinit var tvEstadio: TextView
    private lateinit var tvDireccion: TextView
    private lateinit var tvWebsite: TextView
    private lateinit var tvFundacion: TextView
    private lateinit var tvColores: TextView
    private lateinit var tvEntrenador: TextView

    private val viewModel: TeamDetailViewModel by viewModels()
    private var teamId: Int = 0

    private var currentCompetition: String = "PL"
    private var nombreLiga: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentCompetition = arguments?.getString("COMPETITION") ?: "PL"
        nombreLiga = arguments?.getString("NOMBRE")

        // Recibir el ID del equipo
        teamId = arguments?.getInt("TEAM_ID") ?: 0

        initViews(view)
        setupToolbar()
        setupButtonVolver()
        setupObservers()
        setupMenuProvider()

        // Cargar datos del equipo
        if (teamId != 0) {
            viewModel.cargarDetalleEquipo(teamId)
        } else {
            Toast.makeText(requireContext(), "Error: ID de equipo no válido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        progressBar = view.findViewById(R.id.progressBar)
        btnVolver = view.findViewById(R.id.btnVolver)

        // Inicializar views de datos del equipo
        tvNombre = view.findViewById(R.id.tvNombre)
        tvNombreCorto = view.findViewById(R.id.tvNombreCorto)
        tvEstadio = view.findViewById(R.id.tvEstadio)
        tvDireccion = view.findViewById(R.id.tvDireccion)
        tvWebsite = view.findViewById(R.id.tvWebsite)
        tvFundacion = view.findViewById(R.id.tvFundacion)
        tvColores = view.findViewById(R.id.tvColores)
        tvEntrenador = view.findViewById(R.id.tvEntrenador)
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = nombreLiga
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.teamDetailState.collect { state ->
                when (state) {
                    is TeamDetailViewModel.TeamDetailState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is TeamDetailViewModel.TeamDetailState.Success -> {
                        progressBar.visibility = View.GONE
                        mostrarDatosEquipo(state.teamDetail)
                    }
                    is TeamDetailViewModel.TeamDetailState.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.mensaje, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun mostrarDatosEquipo(teamDetail: TeamDetailDTO) {
        tvNombre.text = teamDetail.name
        tvNombreCorto.text = teamDetail.shortName
        tvEstadio.text = teamDetail.venue
        tvDireccion.text = teamDetail.address
        tvWebsite.text = teamDetail.website
        tvFundacion.text = teamDetail.founded.toString()
        tvColores.text = teamDetail.clubColors

        // Mostrar entrenador si está disponible
        teamDetail.coach?.let { coach ->
            tvEntrenador.text = "${coach.firstName} ${coach.lastName}"
        } ?: run {
            tvEntrenador.text = "No disponible"
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
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LigasFragment())
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