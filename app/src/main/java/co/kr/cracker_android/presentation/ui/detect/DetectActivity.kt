package co.kr.cracker_android.presentation.ui.detect

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityDetectBinding
import co.kr.cracker_android.presentation.util.shortToast
import co.kr.cracker_android.presentation.ui.base.BaseActivity
import timber.log.Timber

class DetectActivity : BaseActivity<ActivityDetectBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_detect
    private lateinit var locationManager: LocationManager
    private var locationByGps: Location? = null
    private var locationByNetwork: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationSystem()
        startCamera()
        initOnClickListener()
    }

    @SuppressLint("MissingPermission")
    private fun initLocationSystem() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val gpsLocationListener = LocationListener { location -> locationByGps = location }
        val networkLocationListener = LocationListener { location -> locationByNetwork = location }
        if (isGpsEnabled) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                10f,
                gpsLocationListener
            )
        }
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                10f,
                networkLocationListener
            )
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.cameraPreview.surfaceProvider) }
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            runCatching {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            }.onFailure {
                Timber.e("Use case binding failed $it")
                shortToast("문제가 발생했습니다.")
                finish()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun initOnClickListener() {
        binding.btnStartDetect.setOnClickListener {
            logCurrentLocation()
        }
    }

    // Already checked in MainActivity
    @SuppressLint("MissingPermission")
    private fun logCurrentLocation() {
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?.let { locationByGps = it }
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            ?.let { locationByNetwork = it }
        var currentLocation: Location? = null
        if (locationByGps != null && locationByNetwork != null) {
            currentLocation =
                if (requireNotNull(locationByGps).accuracy > requireNotNull(locationByNetwork).accuracy) {
                    Timber.tag("currentLocation by").i("locationByGps, both Not Null")
                    locationByGps
                } else {
                    Timber.tag("currentLocation by").i("locationByNetwork, both Not Null")
                    locationByNetwork
                }
        } else if (locationByGps != null) {
            Timber.tag("currentLocation by").i("locationByGps")
            currentLocation = locationByGps
        } else if (locationByNetwork != null) {
            Timber.tag("currentLocation by").i("locationByNetwork")
            currentLocation = locationByNetwork
        }
        currentLocation?.let {
            Timber.tag("currentLocation is").i("위도: ${it.latitude}, 경도: ${it.longitude}")
        } ?: Timber.tag("currentLocation is").i("null")
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DetectActivity::class.java)
        }
    }
}