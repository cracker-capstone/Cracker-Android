package co.kr.cracker_android.presentation.view.main

import android.os.Bundle
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityMainBinding
import co.kr.cracker_android.presentation.view.base.BaseActivity
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main
}