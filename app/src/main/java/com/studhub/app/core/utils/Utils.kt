package com.studhub.app.core.utils

import android.content.Context
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText

class Utils {
    companion object {
        fun displayMessage(
            context: Context,
            message: String?
        ) = makeText(context, message, LENGTH_LONG).show()

    }
}
