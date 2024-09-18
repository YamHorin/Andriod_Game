package com.example.andriod_game.models
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.andriod_game.Interfaces.MoveCallback
import kotlin.math.abs

class MoveDetector (context: Context, private val moveCallback: MoveCallback?){
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private lateinit var sensorEventListener: SensorEventListener
    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[2]
                val z = event.values[0]
                calculateMove(x, z)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // pass
            }

        }
    }
        private fun calculateMove(x: Float, z: Float) {
        if (System.currentTimeMillis() - timestamp >= 200L) {
            timestamp = System.currentTimeMillis()
            moveCallback?.moveX(x)
            if (abs(z) >= 3) {
                moveCallback?.moveZ(z)
            }
        }
    }

    fun stop() {
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }

    fun start() {
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }

}