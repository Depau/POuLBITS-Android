package org.poul.bits.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.poul.bits.android.lib.broadcasts.BitsStatusErrorBroadcast
import org.poul.bits.android.lib.broadcasts.BitsStatusReceivedBroadcast
import org.poul.bits.android.lib.broadcasts.BitsStatusRetrieveStartBroadcast
import org.poul.bits.android.lib.controllers.widgetstorage.impl.SharedPrefsWidgetStorageHelper
import org.poul.bits.android.lib.model.BitsData
import org.poul.bits.android.lib.widgets.getAppWidgetIds
import org.poul.bits.android.ui.widgets.HeadquartersStatusHorizontalWidget
import org.poul.bits.android.ui.widgets.HeadquartersStatusIconAppWidget
import org.poul.bits.android.ui.widgets.HeadquartersStatusWidgetBase

class WidgetUpdateBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BitsStatusRetrieveStartBroadcast.ACTION -> handleStatusRetrieveStartBroadcast(context)
            BitsStatusReceivedBroadcast.ACTION      -> handleStatusReceivedBroadcast(context, intent)
            BitsStatusErrorBroadcast.ACTION         -> handleStatusErrorBroadcast(context)
        }
    }

    private fun requestWidgetUpdate(context: Context) {
        arrayOf(
            HeadquartersStatusHorizontalWidget::class.java,
            HeadquartersStatusIconAppWidget::class.java
        ).forEach { clazz ->
            context.sendBroadcast(
                HeadquartersStatusWidgetBase.getUpdateIntent(
                    context, clazz,
                    getAppWidgetIds(
                        context, clazz
                    )
                )
            )
        }
    }

    private fun handleStatusRetrieveStartBroadcast(context: Context) {
        SharedPrefsWidgetStorageHelper(context).apply {
            loading = true
        }
        requestWidgetUpdate(context)
    }

    private fun handleStatusReceivedBroadcast(context: Context, intent: Intent) {
        val bitsData = intent.getParcelableExtra<BitsData>(
            BitsStatusReceivedBroadcast.BITS_DATA
        )!!
        SharedPrefsWidgetStorageHelper(context).apply {
            this.loading = false
            this.bitsData = bitsData
            this.lastDataUpdate = System.currentTimeMillis()
        }
        requestWidgetUpdate(context)
    }

    private fun handleStatusErrorBroadcast(context: Context) {
        SharedPrefsWidgetStorageHelper(context).apply {
            loading = false
            bitsData = bitsDataError
        }
    }
}
