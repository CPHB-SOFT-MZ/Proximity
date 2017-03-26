# Gyroscope and Proximity
## What is it
- Proximity

It is a sensor that detects if an object is in its proximity.
Proximity sensors are commonly used in smartphones, cars and security systems.
There are different types of sensors for if you want to detect metal, plastic etc.
We will concentrate about the types in phones. In an iPhone 5 they use a Capacitive sensor which detects if anything conductive or dielectric from air.
Which means that if anything is more or less conductive then air it will detect it.
- Gyroscope

It measures the angular velocity. it measures the rational velocity of 3 different directions.
Here is a gif that shows how it works with a gyroscope.

![Gyro](https://github.com/ziemerz/Proximity/blob/master/Gyro.gif)

Here is how we use it with a phone or tablet. 
We have the 3 axises x, y and z.
It is used by the phone to see if it should show you in portrait or landscape.


![PhoneGyro](http://www.geeky-gadgets.com/wp-content/uploads/2014/08/Smartphone-Gyroscope.jpg)

## How can we use it

The gyroscope can be used for games or apps.
The phone generaly uses the gyroscope to see if the phone should use the portrait or landscape layout.
it can also be used for games, take a flight game where when you tilt your phone you should go up and down while if you tilt left and right you should turn left and right.


## How we used it
To register any sensors at all, of the Android device, you need to register a SensorManager.
This makes sure you can request any of the available sensors of the device.
You register a SensorManager by doing the following
```kotlin
val sensorManager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as 
SensorManager
```
### Proximity sensor
Then we’ll try to get the proximity sensor from the manager so we can use it and read values from it.
```kotlin
val proximitySensor : Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
```

Best practice is then to check if the sensor is available. The sensor is usually only not available if broken or if the device doesn’t have a proximity sensor.

```
if(proximitySensor == null){
    Log.e(TAG, "Sensor not available")
    finish()
}
```

When this is done, all that’s left is to make a listener to handle the data we receive from the sensor.

```kotlin
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
```

This listener simply measures the max range and an indicator if the sensor is sensing anything within its proximity. 

Now we need to register this sensor and listener to manager again so it knows what to do with the measured data. We do this with the following code.
```kotlin
sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000)
```
The numeric value specified as the last parameter is the delay for how often the SensorManager should ask the sensor for its values. This is also called polling interval.

### Gyroscope
The gyroscope is a bit more tricky since the returned values doesn’t make much sense when first reading them. This is because the values are actually the velocity around a certain axis.
If we read the SensorEvent array of values, the first value is the measure of velocity of rotation around the X axis, so is the phone flat on a desk or standing up? The second value is the rotation around the Y axis, so is the phone rotated around itself? The last axis, which is value number three is around the Z axis which is the one we use to determine if our phone is tilting clockwise or anticlockwise.

So what we start out doing is getting the sensor from our SensorManager.
```kotlin
val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
```

When we’ve done this, we make a listener just like with the proximity sensor.

```kotlin
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
```

This listener is using the measure of velocity around the Z axis and telling us if the phone is currently moving anticlockwise or clockwise.

This is not so useful to actually detect if the phone is in landscape or portrait or to measure the angle of the phone. For that we have two alternatives: Rotation Vector Sensor and Game Rotation Vector Sensor. 

