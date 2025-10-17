package com.example.appfutbol

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

class LigasFragment : Fragment() {
    private lateinit var btnLogo1: LinearLayout
    private lateinit var btnLogo2: LinearLayout
    private lateinit var btnLogo3: LinearLayout
    private lateinit var btnLogo4: LinearLayout
    private lateinit var btnLogo5: LinearLayout
    private lateinit var btnLogo6: LinearLayout

    private lateinit var btnVolver: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ligas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupToolbar(view)
        setupButtonListeners()
        setupMenuProvider()
    }

    private fun initViews(view: View) {
        btnLogo1 = view.findViewById(R.id.btnLogo1)
        btnLogo2 = view.findViewById(R.id.btnLogo2)
        btnLogo3 = view.findViewById(R.id.btnLogo3)
        btnLogo4 = view.findViewById(R.id.btnLogo4)
        btnLogo5 = view.findViewById(R.id.btnLogo5)
        btnLogo6 = view.findViewById(R.id.btnLogo6)

        btnVolver = view.findViewById(R.id.btnVolver)
    }

    private fun setupToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.titulo)
    }

    private fun setupButtonListeners() {
        btnLogo1.setOnClickListener {
            navigateToListaFragment("PL", "Premier League")
        }

        btnLogo2.setOnClickListener {
            navigateToListaFragment("BL1", "Bundes Liga")
        }

        btnLogo3.setOnClickListener {
            navigateToListaFragment("SA", "Serie A")
        }

        btnLogo4.setOnClickListener {
            navigateToListaFragment("PD", "La Liga")
        }

        btnLogo5.setOnClickListener {
            navigateToListaFragment("PPL", "Primeira Liga")
        }

        btnLogo6.setOnClickListener {
            navigateToListaFragment("FL1", "Ligue 1")
        }

        btnVolver.setOnClickListener {
            // Volver directamente a BienvenidaFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BienvenidaFragment())
                .commit()
        }
    }

    private fun navigateToListaFragment(competition: String, nombre: String) {
        val listaFragment = ListaFragment().apply {
            arguments = Bundle().apply {
                putString("COMPETITION", competition)
                putString("NOMBRE", nombre)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, listaFragment)
            .addToBackStack("ligas")
            .commit()
    }

    private fun setupMenuProvider() {

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)

                // Ocultar ítems del menú
                val listadoItem1 = menu.findItem(R.id.item_listado_ligas)
                listadoItem1?.isVisible = false
                val listadoItem2 = menu.findItem(R.id.item_listado_lista)
                listadoItem2?.isVisible = false
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

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}