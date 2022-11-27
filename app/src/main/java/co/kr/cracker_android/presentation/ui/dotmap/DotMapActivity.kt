package co.kr.cracker_android.presentation.ui.dotmap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityDotMapBinding
import co.kr.cracker_android.presentation.ui.base.BaseActivity
import co.kr.cracker_android.presentation.viewmodel.DotMapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DotMapActivity : BaseActivity<ActivityDotMapBinding>(), OnMapReadyCallback {
    override val layoutRes: Int
        get() = R.layout.activity_dot_map
    private lateinit var mapFragment: SupportMapFragment

    private val viewModel by viewModels<DotMapViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        loadData()
    }

    private fun initView() {
        mapFragment = SupportMapFragment()
        supportFragmentManager.commit {
            replace(R.id.fcv_dot_map, mapFragment)
        }
        mapFragment.getMapAsync(this)
    }

    private fun loadData() {
        viewModel.getDots()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(INHA_LATITUDE, INHA_LONGITUDE), INHA_ZOOM)
        )

        viewModel.dots.observe(this) { dots ->
            dots.forEach { dot ->
                Timber.i(dot.toString())
                googleMap.addMarker(
                    getMarkerOptions(
                        dot.latitude.toDouble(),
                        dot.longitude.toDouble()
                    )
                )
                googleMap.setOnMarkerClickListener { _ ->
                    supportFragmentManager.commit {
                        replace(
                            R.id.fcv_dot_map,
                            DetailDotInfoFragment.newInstance(
                                dot.latitude,
                                dot.longitude
                            )
                        )
                        addToBackStack(null)
                    }
                    true
                }
            }
        }
    }

    private fun getMarkerOptions(lat: Double, lon: Double): MarkerOptions {
        if (lat == 0.0 && lon == 0.0) {
            Timber.tag("LatLng").i("0.0 and 0.0")
            return MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_orange))
                .position(LatLng(INHA_LATITUDE, INHA_LONGITUDE))
        }
        return MarkerOptions()
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_orange))
            .position(LatLng(lat, lon))
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, DotMapActivity::class.java)
        private const val INHA_LATITUDE = 37.4500221
        private const val INHA_LONGITUDE = 126.653488
        private const val INHA_ZOOM = 14F
    }
}