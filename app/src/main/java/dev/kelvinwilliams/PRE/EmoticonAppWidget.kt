package dev.kelvinwilliams.PRE

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

// This class handles all communication and events for your widget.
class EmoticonAppWidget : AppWidgetProvider() {

    // Define an action string for our specific widget click event
    companion object {
        private const val WIDGET_CLICK_ACTION = "dev.kelvinwilliams.PRE.WIDGET_CLICK"
    }

    // Called when the widget is created, updated, or when a click event is received
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform this loop for all widgets belonging to this provider
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    // Called when a custom intent (like our WIDGET_CLICK_ACTION) is broadcast
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (WIDGET_CLICK_ACTION == intent.action) {
            Log.d("Widget", "Emoticon widget clicked!")
            // This is where you would call the function to update the emotion/emoticon
            // For now, we'll just force a manual update.

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                intent.component
            )
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }
}

// Global function used to set up the widget's display and click handler
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // 1. Load the widget layout
    val views = RemoteViews(context.packageName, R.layout.widget_layout)

    // 2. Set the static text (will be replaced by logic later)
    // You can set the text to anything here for testing the display
    views.setTextViewText(R.id.emoticon_text_view, "🙏") 

    // 3. Create a PendingIntent for the click action (Fixes the full-screen window issue)
    val intent = Intent(context, EmoticonAppWidget::class.java).apply {
        action = EmoticonAppWidget.WIDGET_CLICK_ACTION
        // Add unique data to ensure the intent is treated as unique
        data = android.net.Uri.parse("widget://" + appWidgetId)
    }

    val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context, 
        appWidgetId, 
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Use FLAG_IMMUTABLE for modern Android
    )

    // 4. Attach the PendingIntent to the root layout element (R.id.widget_root_layout)
    views.setOnClickPendingIntent(R.id.widget_root_layout, pendingIntent)

    // 5. Tell the AppWidgetManager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
