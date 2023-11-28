package app.divarinterview.android.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import app.divarinterview.android.R
import app.divarinterview.android.utils.gotoFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseView {
    override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup is CoordinatorLayout)
                return viewGroup
            else {
                viewGroup.children.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("RootView must be instance of CoordinatorLayout")
            }
        }
    override val viewContext: Context?
        get() = this

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = createBinding()
        setContentView(_binding?.root)

        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    abstract fun createBinding(): VB

}

abstract class BaseFragment<VB : ViewBinding> : Fragment(), BaseView {
    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout
    override val viewContext: Context?
        get() = context

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}

interface BaseView {
    val rootView: CoordinatorLayout?
    val viewContext: Context?

    fun setWindowProgressIndicator(mustShow: Boolean) {
        rootView?.let {
            viewContext?.let { context ->
                var loadingView = it.findViewById<View>(R.id.loadingView)
                if (loadingView == null && mustShow) {
                    loadingView =
                        LayoutInflater.from(context)
                            .inflate(R.layout.view_window_loading, it, false)
                    it.addView(loadingView)
                }

                loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE
            }
        }
    }

    fun setFrameProgressIndicator(root: ViewGroup, mustShow: Boolean) {
        viewContext?.let { context ->
            var loadingView = root.findViewById<View>(R.id.loadingView)
            if (loadingView == null && mustShow) {
                loadingView =
                    LayoutInflater.from(context)
                        .inflate(R.layout.view_window_loading, root, false)
                root.addView(loadingView)
            }

            loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun errorEvent(exception: BaseException) {
        viewContext?.let { context ->
            when (exception.type) {
                BaseException.Type.AUTHORIZATION -> {
                    gotoFragment(context, R.id.welcomeFragment)

                    showToast(
                        exception.serverMessage ?: context.getString(R.string.error_user_auth)
                    )
                }

                BaseException.Type.TOAST -> {
                    showToast(
                        exception.serverMessage ?: context.getString(exception.localMessage)
                    )
                }

                BaseException.Type.SNACKBAR -> {
                    showSnackbar(
                        exception.serverMessage ?: context.getString(exception.localMessage)
                    )
                }
            }
        }
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        viewContext?.let {
            Toast.makeText(it, message, duration).show()
        }
    }

    fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        rootView?.let {
            Snackbar.make(it, message, duration).show()
        }
    }
}

abstract class BaseViewModel : ViewModel() {
    var windowLoadingState = MutableStateFlow(false)
}


