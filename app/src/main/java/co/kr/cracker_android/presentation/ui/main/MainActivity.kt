package co.kr.cracker_android.presentation.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityMainBinding
import co.kr.cracker_android.presentation.util.shortToast
import co.kr.cracker_android.presentation.ui.base.BaseActivity
import co.kr.cracker_android.presentation.ui.detect.DetectActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    private val requestMultiplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedList: List<String> = result.filterNot { it.value }.map { it.key }
            when {
                deniedList.isNotEmpty() -> {
                    shortToast(getString(R.string.request_permission))
                    val toApplicationDetailsSettings =
                        Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(getString(R.string.scheme_package), packageName, null)
                    toApplicationDetailsSettings.data = uri
                    startActivity(toApplicationDetailsSettings)
                }
                else -> {
                    startActivity(DetectActivity.getIntent(this))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnClickListener()
    }

    private fun initOnClickListener() {
        binding.btnDetect.setOnClickListener {
            val permissionRequest = arrayListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestMultiplePermissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }
}