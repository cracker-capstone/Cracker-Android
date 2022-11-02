package co.kr.cracker_android.presentation.ui.detect

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.FragmentCameraBinding
import co.kr.cracker_android.presentation.ui.base.BaseFragment
import co.kr.cracker_android.presentation.ui.helper.ImageSegmentationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.segmenter.Segmentation
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : BaseFragment<FragmentCameraBinding>(),
    ImageSegmentationHelper.SegmentationListener {
    override val TAG: String
        get() = CameraFragment::class.java.simpleName
    override val layoutRes: Int
        get() = R.layout.fragment_camera

    private lateinit var imageSegmentationHelper: ImageSegmentationHelper
    private lateinit var bitmapBuffer: Bitmap
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraExecutor: ExecutorService
    private var isRecording = false
    private lateinit var locationManager: LocationManager
    private var locationByGps: Location? = null
    private var locationByNetwork: Location? = null
    private var loggingJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImageSegmentationHelper()
        initCameraExecutor()
        postViewFinder()
        initBottomSheetControls()
        initLocationSystem()
        initOnClickListener()
    }

    override fun onStart() {
        super.onStart()
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fcv_detect)
                .navigate(R.id.action_camera_to_permissions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation = binding.viewFinder.display.rotation
    }

    private fun initImageSegmentationHelper() {
        imageSegmentationHelper = ImageSegmentationHelper(
            context = requireContext(),
            imageSegmentationListener = this
        )
    }

    private fun initCameraExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun postViewFinder() {
        binding.viewFinder.post {
            setUpCamera()
        }
    }

    private fun initBottomSheetControls() {
        binding.bottomSheetLayout.threadsMinus.setOnClickListener {
            if (imageSegmentationHelper.numThreads > 1) {
                imageSegmentationHelper.numThreads--
                updateControlsUi()
            }
        }

        binding.bottomSheetLayout.threadsPlus.setOnClickListener {
            if (imageSegmentationHelper.numThreads < 4) {
                imageSegmentationHelper.numThreads++
                updateControlsUi()
            }
        }

        binding.bottomSheetLayout.spinnerDelegate.setSelection(0, false)
        binding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    imageSegmentationHelper.currentDelegate = position
                    updateControlsUi()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationSystem() {
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    private fun initOnClickListener() {
        binding.btnRecord.setOnClickListener {
            if (isRecording) {
                binding.chronometer.format = "녹화 중지: %s"
                binding.chronometer.base = SystemClock.elapsedRealtime()
                binding.chronometer.stop()
                loggingJob?.cancel()
            } else {
                binding.chronometer.format = "녹화 중: %s"
                binding.chronometer.base = SystemClock.elapsedRealtime()
                binding.chronometer.start()
                logCurrentLocation()
            }
            isRecording = !isRecording
        }
    }

    //    private fun initOnClickListener() {
//        binding.btnStartDetect.setOnClickListener {
//            logCurrentLocation()
//        }
//    }

    // Already checked in MainActivity
    @SuppressLint("MissingPermission")
    private fun logCurrentLocation() {
        loggingJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(1000)
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?.let { locationByGps = it }
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        ?.let { locationByNetwork = it }
                    var currentLocation: Location? = null
                    if (locationByGps != null && locationByNetwork != null) {
                        currentLocation =
                            if (requireNotNull(locationByGps).accuracy > requireNotNull(
                                    locationByNetwork
                                ).accuracy
                            ) {
                                Timber.tag("currentLocation by").i("locationByGps, both Not Null")
                                locationByGps
                            } else {
                                Timber.tag("currentLocation by")
                                    .i("locationByNetwork, both Not Null")
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
                        Timber.tag("currentLocation is")
                            .i("위도: ${it.latitude}, 경도: ${it.longitude}")
                    } ?: Timber.tag("currentLocation is").i("null")
                }
            }
        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun updateControlsUi() {
        binding.bottomSheetLayout.threadsValue.text =
            imageSegmentationHelper.numThreads.toString()

        imageSegmentationHelper.clearImageSegmenter()
        binding.overlay.clear()
    }

    private fun bindCameraUseCases() {
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .build()

        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }

                        segmentImage(image)
                    }
                }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Timber.tag("Image Segmentation").e(exc, "Use case binding failed")
        }
    }

    private fun segmentImage(image: ImageProxy) {
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
        val imageRotation = image.imageInfo.rotationDegrees
        imageSegmentationHelper.segment(bitmapBuffer, imageRotation)
    }

    override fun onResults(
        results: List<Segmentation>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {
        requireActivity().runOnUiThread {
            binding.bottomSheetLayout.inferenceTimeVal.text = String.format("%d ms", inferenceTime)
            binding.overlay.setResults(results, imageHeight, imageWidth)
            binding.overlay.invalidate()
        }
    }

    override fun onError(error: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }
}