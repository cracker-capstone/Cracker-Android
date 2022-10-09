package co.kr.cracker_android.presentation.view.detect

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityDetectBinding
import co.kr.cracker_android.presentation.util.shortToast
import co.kr.cracker_android.presentation.view.base.BaseActivity
import timber.log.Timber

class DetectActivity : BaseActivity<ActivityDetectBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_detect
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        startCamera()
        initOnClickListener()
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
        val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        Timber.tag("location")
            .i("위도: ${currentLocation?.latitude}, 경도: ${currentLocation?.longitude}")
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DetectActivity::class.java)
        }
    }
}