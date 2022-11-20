package co.kr.cracker_android.presentation.ui.main

import android.os.Bundle
import co.kr.cracker_android.R
import co.kr.cracker_android.data.preferences.CrackerSharedPreferences
import co.kr.cracker_android.databinding.ActivityMainBinding
import co.kr.cracker_android.presentation.ui.base.BaseActivity
import co.kr.cracker_android.presentation.ui.detect.DetectActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    @Inject
    lateinit var preferences: CrackerSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnClickListener()
        initUuid()
    }

    private fun initOnClickListener() {
        binding.btnDetect.setOnClickListener {
            startActivity(DetectActivity.getIntent(this))
        }
    }

    private fun initUuid() {
        if (preferences.uuid == "") {
            preferences.uuid = UUID.randomUUID().toString()
        }
    }
}