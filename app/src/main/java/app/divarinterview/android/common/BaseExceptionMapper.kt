package app.divarinterview.android.common

import androidx.annotation.StringRes
import app.divarinterview.android.R

class BaseExceptionMapper {
    companion object {
        fun httpExceptionMapper(
            errorCode: Int,
            @StringRes localMessage: Int = 0,
            serverMessage: String? = null
        ): BaseException {
            try {
                return when (errorCode) {
                    401 -> {
                        BaseException(
                            BaseException.Type.AUTHORIZATION,
                            serverMessage = serverMessage,
                            localMessage = localMessage
                        )
                    }

                    else -> {
                        BaseException(
                            BaseException.Type.TOAST,
                            localMessage = localMessage
                        )
                    }
                }
            } catch (exception: Exception) {
                return BaseException(
                    BaseException.Type.TOAST,
                    localMessage = R.string.error_unknown
                )
            }
        }
    }
}