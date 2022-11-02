package co.kr.cracker_android.presentation.ui.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter
import org.tensorflow.lite.task.vision.segmenter.OutputType
import org.tensorflow.lite.task.vision.segmenter.Segmentation
import timber.log.Timber

class ImageSegmentationHelper(
    var numThreads: Int = 2,
    var currentDelegate: Int = 0,
    val context: Context,
    val imageSegmentationListener: SegmentationListener?
) {
    private var imageSegmenter: ImageSegmenter? = null

    init {
        setupImageSegmenter()
    }

    fun clearImageSegmenter() {
        imageSegmenter = null
    }

    private fun setupImageSegmenter() {
        val optionsBuilder =
            ImageSegmenter.ImageSegmenterOptions.builder()
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThreads)

        when (currentDelegate) {
            DELEGATE_CPU -> {

            }
            DELEGATE_GPU -> {
                if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                    baseOptionsBuilder.useGpu()
                } else {
                    imageSegmentationListener?.onError("GPU is not supported on this device")
                }
            }
            DELEGATE_NNAPI -> {
                baseOptionsBuilder.useNnapi()
            }
        }

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        optionsBuilder.setOutputType(OutputType.CATEGORY_MASK)
        try {
            imageSegmenter =
                ImageSegmenter.createFromFileAndOptions(
                    context,
                    MODEL_DEEPLABV3,
                    optionsBuilder.build()
                )
        } catch (e: IllegalStateException) {
            imageSegmentationListener?.onError(
                "Image segmentation failed to initialize. See error logs for details"
            )
            Timber.tag(TAG).e("TFLite failed to load model with error: ${e.message}")
        }
    }

    fun segment(image: Bitmap, imageRotation: Int) {

        if (imageSegmenter == null) {
            setupImageSegmenter()
        }

        var inferenceTime = SystemClock.uptimeMillis()

        val imageProcessor =
            ImageProcessor.Builder()
                .add(Rot90Op(-imageRotation / 90))
                .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
                .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))
        val segmentResult = imageSegmenter?.segment(tensorImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        imageSegmentationListener?.onResults(
            segmentResult,
            inferenceTime,
            tensorImage.height,
            tensorImage.width
        )
    }

    interface SegmentationListener {
        fun onError(error: String)
        fun onResults(
            results: List<Segmentation>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int
        )
    }

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2
        const val MODEL_DEEPLABV3 = "unet_tflite_quant_models_metadata.tflite"

        private const val TAG = "Image Segmentation Helper"
    }
}
