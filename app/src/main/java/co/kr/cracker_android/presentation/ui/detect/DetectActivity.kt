package co.kr.cracker_android.presentation.ui.detect

import android.content.Context
import android.content.Intent
import android.os.Build
import co.kr.cracker_android.R
import co.kr.cracker_android.databinding.ActivityDetectBinding
import co.kr.cracker_android.presentation.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetectActivity : BaseActivity<ActivityDetectBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_detect

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
            // (https://issuetracker.google.com/issues/139738913)
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DetectActivity::class.java)
        }
    }
}