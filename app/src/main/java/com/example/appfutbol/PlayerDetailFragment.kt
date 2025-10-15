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
import dtos.PlayerDetailDTO
import kotlinx.coroutines.launch
import viewmodels.PlayerDetailViewModel

class PlayerDetailFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var btnVolver: Button

    // Views para mostrar los datos del jugador
    private lateinit var tvNombre: TextView
    private lateinit var tvNombreCompleto: TextView
    private lateinit var tvFechaNacimiento: TextView
    private lateinit var tvNacionalidad: TextView
    private lateinit var tvPosicion: TextView

    private val viewModel: PlayerDetailViewModel by viewModels()
    private var playerId: Int = 0

    private var currentCompetition: String = "PL"
    private var nombreLiga: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentCompetition = arguments?.getString("COMPETITION") ?: "PL"
        nombreLiga = arguments?.getString("NOMBRE")

        // Recibir el ID del jugador
        playerId = arguments?.getInt("PLAYER_ID") ?: 0

        initViews(view)
        setupToolbar()
        setupButtonVolver()
        setupObservers()
        setupMenuProvider()

        // Cargar datos del jugador
        if (playerId != 0) {
            viewModel.cargarDetalleJugador(playerId)
        } else {
            Toast.makeText(requireContext(), "Error: ID de jugador no válido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        progressBar = view.findViewById(R.id.progressBar)
        btnVolver = view.findViewById(R.id.btnVolver)

        // Inicializar views de datos del jugador
        tvNombre = view.findViewById(R.id.tvNombre)
        tvNombreCompleto = view.findViewById(R.id.tvNombreCompleto)
        tvFechaNacimiento = view.findViewById(R.id.tvFechaNacimiento)
        tvNacionalidad = view.findViewById(R.id.tvNacionalidad)
        tvPosicion = view.findViewById(R.id.tvPosicion)
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
            viewModel.playerDetailState.collect { state ->
                when (state) {
                    is PlayerDetailViewModel.PlayerDetailState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is PlayerDetailViewModel.PlayerDetailState.Success -> {
                        progressBar.visibility = View.GONE
                        mostrarDatosJugador(state.playerDetail)
                    }
                    is PlayerDetailViewModel.PlayerDetailState.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.mensaje, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun mostrarDatosJugador(playerDetail: PlayerDetailDTO) {
        tvNombre.text = playerDetail.name
        tvNombreCompleto.text = "${playerDetail.firstName} ${playerDetail.lastName}"
        tvFechaNacimiento.text = playerDetail.dateOfBirth
        tvNacionalidad.text = playerDetail.nationality
        tvPosicion.text = playerDetail.section
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