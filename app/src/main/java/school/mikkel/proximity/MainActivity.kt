package school.mikkel.proximity

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sensorManager

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sensorManager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val proximitySensor : Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if(proximitySensor == null){
            Log.e(TAG, "Sensor not available")
            finish()
        }

        val proximitySensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent?) {
                var values = event?.values
                if(values != null){
                    val range = proximitySensor.maximumRange
                    var x = values[0]
                    range_view.text = range.toString()
                    value_view.text = x.toString()
                }
            }

        }

        val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        val gyroscopeListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

            override fun onSensorChanged(p0: SensorEvent?) {
                var value = p0!!.values[2]

                if(value > 0.5f) {
                    gyro_value.text = "Anticlockwise"
                } else if(value < -0.5f){
                    gyro_value.text = "Clockwise"
                }

            }

        }

        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val rvListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

            override fun onSensorChanged(p0: SensorEvent?) {
                var rotationMatrix : FloatArray = FloatArray(16)
                var remappedOrientationMatrix : FloatArray = FloatArray(16)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, p0!!.values)
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Y,
                        remappedOrientationMatrix)
                var orientations : FloatArray = FloatArray(16)
                SensorManager.getOrientation(remappedOrientationMatrix, orientations)
                var x = 0
                while(x < 3){
                    orientations[x] = Math.toDegrees(orientations[x].toDouble()).toFloat()
                    x++
                }
                gyro2_value.text = orientations[2].toString()
            }

        }

        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000)
        sensorManager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(rvListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

}
