package co.kr.cracker_android.presentation.ui.detect

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.FragmentPermissionsBinding
import co.kr.cracker_android.presentation.ui.base.BaseFragment
import co.kr.cracker_android.util.shortToast

class PermissionsFragment : BaseFragment<FragmentPermissionsBinding>() {
    override val TAG: String
        get() = PermissionsFragment::class.java.simpleName
    override val layoutRes: Int
        get() = R.layout.fragment_permissions

    private val requestMultiplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedList: List<String> = result.filterNot { it.value }.map { it.key }
            when {
                deniedList.isNotEmpty() -> {
                    requireContext().shortToast(getString(R.string.request_permission))
                    val toApplicationDetailsSettings =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        getString(R.string.scheme_package),
                        requireContext().packageName,
                        null
                    )
                    toApplicationDetailsSettings.data = uri
                    startActivity(toApplicationDetailsSettings)
                }
                else -> {
                    startActivity(DetectActivity.getIntent(requireContext()))
                }
            }
        }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    private fun checkPermission() {
        if (hasPermissions(requireContext())) {
            navigateToCamera()
        } else {
            requestMultiplePermissionLauncher.launch(PERMISSIONS_REQUIRED.toTypedArray())
        }
    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fcv_detect)
                .navigate(R.id.action_permissions_to_camera)
        }
    }

    companion object {
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        private val PERMISSIONS_REQUIRED = arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}