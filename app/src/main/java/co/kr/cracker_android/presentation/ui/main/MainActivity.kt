package co.kr.cracker_android.presentation.ui.main

import android.os.Bundle
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityMainBinding
import co.kr.cracker_android.presentation.ui.base.BaseActivity
import co.kr.cracker_android.presentation.ui.detect.DetectActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnClickListener()
    }

    private fun initOnClickListener() {
        binding.btnDetect.setOnClickListener {
            startActivity(DetectActivity.getIntent(this))
        }
    }
}