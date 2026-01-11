package dev.kelvinwilliams.PRE

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlin.random.Random // FIX 2: Added required import

// This class handles all communication and events for your widget.
class EmoticonAppWidget : AppWidgetProvider() {

    // Emoticon array is a private property of the class
    private val EMOTICONS = arrayOf(
        "🙏", "🖕", "👇", "👆", "☝️", "👈", "👉", "🫵", "👌", "🤏", 
        "🤌", "🤙", "🫰", "🤞", "✌️", "🤘", "🤟", "🖖", "✋", "🖐️", 
        "🤚", "👋", "🫷", "🫸", "🫲", "🫱", "🫴", "🫳", "👊", "✊", 
        "🤛", "🤜", "🤲", "👐", "🙌", "🫶", "👎", "👍", "👏", "💪", 
        "👃", "👂", "🦻", "🦶", "🦵"
    )
      
    // Define an action string for our specific widget click event
    companion object {
        // FIX 4: Removed 'private' to allow access from updateAppWidget
        const val WIDGET_CLICK_ACTION = "dev.kelvinwilliams.PRE.WIDGET_CLICK" 
    }
    
    // FIX 1: Moved inside the class to access EMOTICONS
    private fun getShuffledEmoticon(): String {
        val tempArray = EMOTICONS.toMutableList()
        
        // Determine a random number of shuffles
        val numShuffles = Random.nextInt(1, 6) 

        // Shuffle the array 'numShuffles' times
        repeat(numShuffles) {
            tempArray.shuffle()
        }
        
        // Select and return a random element from the result
        val randomIndex = Random.nextInt(tempArray.size)
        return tempArray[randomIndex]
    }

    // FIX 3: Moved inside the class, removed 'override'
    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Use the shuffled emoticon
        val currentEmoticon = getShuffledEmoticon()
        
        views.setTextViewText(R.id.emoticon_text_view, currentEmoticon)

        val intent = Intent(context, EmoticonAppWidget::class.java).apply {
            action = WIDGET_CLICK_ACTION
            // Add unique data to ensure the intent is treated as unique
            data = android.net.Uri.parse("widget://" + appWidgetId)
        }

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 
            appWidgetId, 
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE 
        )

        views.setOnClickPendingIntent(R.id.widget_root_layout, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
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
            // This forces a refresh of the widget, triggering a new random emoticon
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                intent.component
            )
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }
}
            tempArray.shuffle()
        }
        
        // Select and return a random element from the result
        val randomIndex = Random.nextInt(tempArray.size)
        return tempArray[randomIndex]
    }
    
// new
  override fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Use the passed emoticon or default to the first element ("🙏")
        // for initial placement, satisfying the requirement.
        val currentEmoticon = getShuffledEmoticon()
        
        views.setTextViewText(R.id.emoticon_text_view, currentEmoticon)

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
