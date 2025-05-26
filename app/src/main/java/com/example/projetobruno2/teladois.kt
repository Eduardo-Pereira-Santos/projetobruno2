package com.example.projetobruno2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projetobruno2.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class teladois : AppCompatActivity() {

    private val client = OkHttpClient()
    private val apiKey = "AIzaSyC4TtxnNQwH8P4iSG8cOsAUzxFaJ6PlbhU"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teladois)

        val botaoGerarPedido = findViewById<Button>(R.id.botaoGerarPedido)
        val botaoGerarPresente = findViewById<Button>(R.id.botaoGerarPresente)
        val resultadoPedido = findViewById<TextView>(R.id.resultadoPedido)
        val resultadoPresente = findViewById<TextView>(R.id.resultadoPresente)

        botaoGerarPedido.setOnClickListener {
            val promptPedido = "Faça um pedido de desculpas para uma namorada(o)"
            consultarGemini(promptPedido) { resposta ->
                runOnUiThread { resultadoPedido.text = resposta }
            }
        }

        botaoGerarPresente.setOnClickListener {
            val promptPresente = "Fale um presente para pedir desculpas para namorada(o)"
            consultarGemini(promptPresente) { resposta ->
                runOnUiThread { resultadoPresente.text = resposta }
            }
        }
    }

    private fun consultarGemini(pergunta: String, callback: (String) -> Unit) {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray().put(
                    JSONObject().put("text", pergunta)
                ))
            ))
        }

        val corpo = json.toString().toRequestBody("application/json".toMediaType())

        val requisicao = Request.Builder()
            .url(url)
            .post(corpo)
            .build()

        client.newCall(requisicao).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Erro: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val resposta = response.body?.string() ?: "Sem resposta"
                try {
                    val jsonResposta = JSONObject(resposta)
                    val texto = jsonResposta
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    callback(texto)
                } catch (e: Exception) {
                    callback("Erro ao ler resposta: ${e.message}")
                }
            }
        })
    }
}