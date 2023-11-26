package app.divarinterview.android.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import app.divarinterview.android.common.BaseActivity
import app.divarinterview.android.databinding.ActivityMainBinding
import app.divarinterview.android.ui.common.BaseFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var fragmentFactory: BaseFragmentFactory

    private lateinit var splashScreen: SplashScreen;

    private var keep = true
    private val delay = 1000L

    override fun createBinding(): ActivityMainBinding {
        splashScreen = installSplashScreen()
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory

        splashScreen.setKeepOnScreenCondition { keep }
        Handler(Looper.getMainLooper()).postDelayed({ keep = false }, delay)
    }
}