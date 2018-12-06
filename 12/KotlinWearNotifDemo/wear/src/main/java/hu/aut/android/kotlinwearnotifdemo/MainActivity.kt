package hu.aut.android.kotlinwearnotifdemo

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity(), DataClient.OnDataChangedListener, MessageClient.OnMessageReceivedListener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        btnFlame.setOnClickListener {
            Toast.makeText(this, "Wear Toast", Toast.LENGTH_LONG).show()

            setContentView(FlameView(this))
        }

        btnRing.setOnClickListener {
            sendRingToneMessage()
        }
    }

    override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
    }

    override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
        Wearable.getMessageClient(this).removeListener(this)
    }

    override fun onDataChanged(p0: DataEventBuffer) {
        Toast.makeText(this, "message received", Toast.LENGTH_LONG).show()
    }

    override fun onMessageReceived(p0: MessageEvent) {
        Toast.makeText(this, "message received "+p0.path, Toast.LENGTH_LONG).show()
    }

    private fun sendRingToneMessage() {
        Thread {
            val nodeListTask = Wearable.getNodeClient(applicationContext).connectedNodes
            val nodes = Tasks.await(nodeListTask)

            runOnUiThread {
                nodes.forEach {
                    Toast.makeText(this@MainActivity, it.displayName, Toast.LENGTH_LONG).show()
                    Wearable.getMessageClient(this).sendMessage(it.id, "/ring", "Data".toByteArray())
                }
            }
        }.start()
    }
}
