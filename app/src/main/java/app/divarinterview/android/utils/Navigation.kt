package app.divarinterview.android.utils

import android.content.Context
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import app.divarinterview.android.R

fun gotoFragment(context: Context, @IdRes destinationId: Int) {
    val navHostFragment =
        (context as FragmentActivity).supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
    val navController = navHostFragment.navController

    navController.popBackStack(destinationId, false)
}