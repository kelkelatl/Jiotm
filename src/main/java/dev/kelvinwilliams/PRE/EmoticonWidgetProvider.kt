package dev.kelvinwilliams.PRE

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlin.random.Random

class EmoticonWidgetProvider : AppWidgetProvider() {

    // --- 1. Emoticon Array and Constants ---

    private val EMOTICONS = arrayOf(
        "🙏", "🖕", "👇", "👆", "☝️", "👈", "👉", "🫵", "👌", "🤏", 
        "🤌", "🤙", "🫰", "🤞", "✌️", "🤘", "🤟", "🖖", "✋", "🖐️", 
        "🤚", "👋", "🫷", "🫸", "🫲", "🫱", "🫴", "🫳", "👊", "✊", 
        "🤛", "🤜", "🤲", "👐", "🙌", "🫶", "👎", "👍", "👏", "💪", 
        "👃", "👂", "🦻", "🦶", "🦵"
    )

    companion object {
        // Custom action for handling widget taps
        private const val ACTION_WIDGET_TAP = "dev.kelvinwilliams.PRE.EMOTICON_WIDGET_TAP"
    }

    // --- 2. Core Logic: Shuffle and Select ---

    /**
     * Shuffles the array a random number of times (1 to 5) and returns a random emoticon.
     */
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

    // --- 3. Widget Update Function ---

    /**
     * Updates a single widget instance with the specified or default emoticon.
     */
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        emoticonToDisplay: String? = null 
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Use the passed emoticon or default to the first element ("🙏")
        // for initial placement, satisfying the requirement.
        val currentEmoticon = emoticonToDisplay ?: EMOTICONS.first() 
        
        views.setTextViewText(R.id.emoticon_text_view, currentEmoticon)

        // Create the Intent for the widget tap
        val intent = Intent(context, EmoticonWidgetProvider::class.java).apply {
            action = ACTION_WIDGET_TAP
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }

        // Wrap the Intent in a PendingIntent
        val pendingIntent = PendingIntent.getBroadcast(
            context, 
            appWidgetId, 
            intent, 
            // FLAG_UPDATE_CURRENT is needed to update the extras if the widget ID changes,
            // FLAG_IMMUTABLE is required for newer Android versions.
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE 
        )

        // Attach the PendingIntent to the root view of the widget layout
        views.setOnClickPendingIntent(R.id.widget_root_layout, pendingIntent)

        // Tell the AppWidgetManager to perform the update
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    // --- 4. AppWidgetProvider Lifecycle Overrides ---

    /**
     * Called when the widget is created/placed and for scheduled updates (if any).
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widget instances
        for (appWidgetId in appWidgetIds) {
            // This call uses the default value (EMOTICONS.first()) for initial placement.
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * Called when a broadcast is received, including the custom tap action.
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_WIDGET_TAP) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, 
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                
                // Get the newly shuffled emoticon
                val newEmoticon = getShuffledEmoticon()
                
                // Update the widget with the new emoticon
                updateAppWidget(context, appWidgetManager, appWidgetId, newEmoticon)
            }
        }
    }
}
