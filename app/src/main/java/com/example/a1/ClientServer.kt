package com.example.a1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.concurrent.Executors

class ClientServer : AppCompatActivity() {

    private lateinit var btnSend: Button
    private lateinit var tvStatus: TextView
    private lateinit var tvResponse: TextView
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_server)

        // Находим все элементы по id
        btnSend = findViewById(R.id.btnSend)
        tvStatus = findViewById(R.id.tvStatus)
        tvResponse = findViewById(R.id.tvResponse)

        btnSend.setOnClickListener {
            sendMessageToServer()
        }
    }

    private fun sendMessageToServer() {
        tvStatus.text = "Отправка данных..."
        btnSend.isEnabled = false

        executor.execute {
            try {
                val context = ZContext()
                val socket = context.createSocket(SocketType.REQ)

                // Подключаемся к серверу
                socket.connect("tcp://172.20.10.3:2222")

                // Отправляем данные на сервер
                val message = "Hello from Android!"
                socket.send(message.toByteArray(ZMQ.CHARSET), 0)

                // Получаем ответ от сервера
                val replyBytes = socket.recv(0)
                val reply = String(replyBytes, ZMQ.CHARSET)

                runOnUiThread {
                    tvStatus.text = "Данные отправлены"
                    tvResponse.text = "Ответ сервера: $reply"
                    Toast.makeText(this, "Успешно отправлено", Toast.LENGTH_SHORT).show()
                }

                socket.close()
                context.close()

            } catch (e: Exception) {
                runOnUiThread {
                    tvStatus.text = "Ошибка подключения"
                    tvResponse.text = "Ошибка: ${e.message}"
                    Toast.makeText(this, "Ошибка отправки", Toast.LENGTH_SHORT).show()
                }
            } finally {
                runOnUiThread {
                    btnSend.isEnabled = true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdownNow()
    }
}