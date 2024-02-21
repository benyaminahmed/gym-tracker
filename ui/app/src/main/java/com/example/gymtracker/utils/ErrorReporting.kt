package com.example.gymtracker.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object ErrorReporting {
    private var currentSnackbar: Snackbar? = null

    fun showError(errorMessage: String, view: View) {
        // Dismiss any currently displayed Snackbar before showing a new one
        currentSnackbar?.dismiss()

        val snackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("DISMISS") { snackbar.dismiss() }

        // Keep track of the current Snackbar
        currentSnackbar = snackbar

        // Ensure the reference to the current Snackbar is cleared when it is dismissed
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                // Clear the reference when the Snackbar is no longer visible
                if (currentSnackbar === transientBottomBar) {
                    currentSnackbar = null
                }
            }
        })

        snackbar.show()
    }
}
