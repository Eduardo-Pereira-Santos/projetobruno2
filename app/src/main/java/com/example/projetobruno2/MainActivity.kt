package com.example.projetobruno2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projetobruno2.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputSenha: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnEntrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        inputEmail = findViewById(R.id.inputEmail)
        inputSenha = findViewById(R.id.inputSenha)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnEntrar = findViewById(R.id.btnEntrar)

        btnRegistrar.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val senha = inputSenha.text.toString().trim()
            registrarUsuario(email, senha)
        }

        btnEntrar.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val senha = inputSenha.text.toString().trim()
            autenticarUsuario(email, senha)
        }
    }

    private fun registrarUsuario(email: String, senha: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                val msg = if (task.isSuccessful) "Registrado com sucesso!"
                else "Erro: ${task.exception?.message}"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
    }

    private fun autenticarUsuario(email: String, senha: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, teladois::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao logar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}