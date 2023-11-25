package app.divarinterview.android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private var keep = true
    private val delay = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)

        splashScreen.setKeepOnScreenCondition { keep }
        Handler(Looper.getMainLooper()).postDelayed({ keep = false }, delay)
    }
}