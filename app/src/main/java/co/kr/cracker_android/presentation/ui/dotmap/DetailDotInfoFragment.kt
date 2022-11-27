package co.kr.cracker_android.presentation.ui.dotmap

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.FragmentDetailDotInfoBinding
import co.kr.cracker_android.presentation.ui.base.BaseFragment
import co.kr.cracker_android.presentation.viewmodel.DotMapViewModel
import co.kr.cracker_android.util.decode

class DetailDotInfoFragment : BaseFragment<FragmentDetailDotInfoBinding>() {
    override val TAG: String
        get() = DetailDotInfoFragment::class.java.simpleName
    override val layoutRes: Int
        get() = R.layout.fragment_detail_dot_info

    private val latitude by lazy { requireArguments().getFloat(KEY_LATITUDE) }
    private val longitude by lazy { requireArguments().getFloat(KEY_LONGITUDE) }
    private val viewModel by activityViewModels<DotMapViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        observeData()
    }

    private fun loadData() {
        viewModel.getDot(latitude, longitude)
    }

    private fun observeData() {
        viewModel.dot.observe(viewLifecycleOwner) {
            val originalImage = it.originalImage.decode()
            val predictionImage = it.predictionImage.decode()
            binding.ivTrue.setImageBitmap(originalImage)
            binding.ivPred.setImageBitmap(predictionImage)
            binding.ivUnderlay.setImageBitmap(originalImage)
            binding.ivOverlay.setImageBitmap(predictionImage)
            binding.tvPercent.text = "${it.crackRatio}%"
        }
    }

    companion object {
        private const val KEY_LATITUDE = "LATITUDE"
        private const val KEY_LONGITUDE = "LONGITUDE"
        fun newInstance(latitude: Float, longitude: Float) = DetailDotInfoFragment().apply {
            arguments = Bundle().apply {
                putFloat(KEY_LATITUDE, latitude)
                putFloat(KEY_LONGITUDE, longitude)
            }
        }
    }
}