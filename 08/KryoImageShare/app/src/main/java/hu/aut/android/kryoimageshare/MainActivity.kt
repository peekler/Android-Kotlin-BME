package hu.aut.android.kryoimageshare

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import kotlinx.android.synthetic.main.activity_main.*
import com.esotericsoftware.kryonet.Client
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import java.io.ByteArrayOutputStream
import java.lang.Exception
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import com.esotericsoftware.minlog.Log


class MainActivity : AppCompatActivity() {

    private var server = Server(131072, 131072)
    private var serverConnection: Connection? = null
    private var client = Client(131072, 131072)

    private var serverMode = false

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.set(Log.LEVEL_DEBUG)

        btnStart.setOnClickListener {
            serverMode = true
            Thread {
                val kryo = server.kryo
                kryo.register(TextMessage::class.java)
                kryo.register(ImageMessage::class.java)
                kryo.register(ByteArray::class.java)

                server.addListener(object : Listener() {
                    override fun connected(connection: Connection?) {
                        super.connected(connection)
                        serverConnection = connection

                        runOnUiThread {
                            btnSendTextMessage.isEnabled = true
                            btnSendImage.isEnabled = true
                        }
                    }

                    override fun received(connection: Connection, obj: Any) {
                        handleNetworkMessage(obj)
                    }

                    override fun disconnected(connection: Connection?) {
                        super.disconnected(connection)
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "DISCONNECTED", Toast.LENGTH_LONG).show();
                        }
                    }
                })

                server.start()
                server.bind(54555, 54777)
            }.start()
        }

        btnConnect.setOnClickListener {
            serverMode = false

            Thread {
                client.start()
                val kryo = client.kryo
                kryo.register(TextMessage::class.java)
                kryo.register(ImageMessage::class.java)
                kryo.register(ByteArray::class.java)
                val address = client.discoverHost(54777, 20000)
                runOnUiThread {
                    tvStatus.text = "discovered address: ${address.hostAddress}"

                }

                client.addListener(object : Listener() {
                    override fun connected(connection: Connection?) {
                        super.connected(connection)
                        runOnUiThread {
                            btnSendTextMessage.isEnabled = true
                            btnSendImage.isEnabled = true
                        }
                    }

                    override fun received(connection: Connection, obj: Any) {
                        handleNetworkMessage(obj)
                    }

                    override fun disconnected(connection: Connection?) {
                        super.disconnected(connection)
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "DISCONNECTED", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                client.connect(20000, address, 54555, 54777)
            }.start()
        }

        btnSendTextMessage.setOnClickListener {
            Thread {
                val textMessage = TextMessage()
                textMessage.text = "msg from server ${System.currentTimeMillis()}"

                when {
                    serverMode -> serverConnection?.sendTCP(textMessage)
                    else -> client.sendTCP(textMessage)
                }
            }.start()
        }

        btnSendImage.setOnClickListener {
            try {
                val stream = ByteArrayOutputStream()
                val bitmap = (ivPhoto.drawable as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, stream)
                val byteArray = stream.toByteArray()
                val imageMessage = ImageMessage()
                imageMessage.image = byteArray
                Thread {
                    try {
                        when {
                            serverMode -> {
                                val sendTCP = serverConnection?.sendTCP(imageMessage)
                                runOnUiThread {
                                    Toast.makeText(this@MainActivity, "Sent: $sendTCP", Toast.LENGTH_LONG).show()
                                }
                            }

                            else -> {
                                val sendTCP: Any = client.sendTCP(imageMessage)
                                runOnUiThread {
                                    Toast.makeText(this@MainActivity, "Sent: $sendTCP", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            }catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        btnPhoto.setOnClickListener {
            takePhoto()
        }


    }

    private fun handleNetworkMessage(obj: Any) {
        when (obj) {
            is TextMessage -> runOnUiThread {
                tvStatus.text = "message: ${obj.text}"
            }
            is ImageMessage -> runOnUiThread {
                val bitmap = BitmapFactory.decodeByteArray(obj.image, 0, obj.image!!.size)
                ivPhoto.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            server.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            client.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @WithPermissions(
            permissions = [Manifest.permission.CAMERA]
    )
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            data?.also {
                val imageBitmap = it.extras.get("data") as Bitmap
                ivPhoto.setImageBitmap(imageBitmap)
            }
        }
    }
}

class TextMessage {
    var text: String? = null
}

class ImageMessage {
    var image: ByteArray? = null
}