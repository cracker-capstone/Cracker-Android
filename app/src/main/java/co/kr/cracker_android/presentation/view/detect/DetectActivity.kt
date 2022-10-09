package co.kr.cracker_android.presentation.view.detect

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityDetectBinding
import co.kr.cracker_android.presentation.view.base.BaseActivity
import timber.log.Timber

class DetectActivity : BaseActivity<ActivityDetectBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_detect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCamera()
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
            }
        }, ContextCompat.getMainExecutor(this))
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DetectActivity::class.java)
        }
    }
}