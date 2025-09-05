package com.example.appfutbol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var btnRegistrarme: Button
    lateinit var btnIniciar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRegistrarme=findViewById<Button>(R.id.btnRegistro)
        btnIniciar=findViewById<Button>(R.id.btnIniciar)

        btnRegistrarme.setOnClickListener {

            //TODO: INTENT A REGISTRO
        }

        btnRegistrarme.setOnClickListener {

            //TODO: INTENT A LOGIN
        }
    }
}