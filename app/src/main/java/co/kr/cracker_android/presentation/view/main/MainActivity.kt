package co.kr.cracker_android.presentation.view.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityMainBinding
import co.kr.cracker_android.presentation.util.shortToast
import co.kr.cracker_android.presentation.view.base.BaseActivity
import co.kr.cracker_android.presentation.view.detect.DetectActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startActivity(DetectActivity.getIntent(this))
            } else {
                shortToast(getString(R.string.no_permission_for_camera))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnClickListener()
    }

    private fun initOnClickListener() {
        binding.btnDetect.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        when {
            checkSelfPermissionGranted() -> {
                startActivity(DetectActivity.getIntent(this))
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showInContextUI()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun checkSelfPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun showInContextUI() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.need_permission_title))
            .setMessage(getString(R.string.message_for_camera_permission))
            .setPositiveButton(getString(R.string.agree)) {_, _ ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }.setNegativeButton(getString(R.string.disagree)) {_, _ ->
                shortToast(getString(R.string.no_permission_for_camera))
            }.create()
            .show()
    }
}