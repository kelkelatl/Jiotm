package dev.kelvinwilliams.PRE

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
//import kotlin.random.Random // Required import for Random class

class EmoticonAppWidget : AppWidgetProvider() {

    // --- CLASS PROPERTIES ---
    private val EMOTICONS = arrayOf(
        "🙏", "🖕", "👇", "👆", "☝️", "👈", "👉", "🫵", "👌", "🤏", 
        "🤌", "🤙", "🫰", "🤞", "✌️", "🤘", "🤟", "🖖", "✋", "🖐️", 
        "🤚", "👋", "🫷", "🫸", "🫲", "🫱", "🫴", "🫳", "👊", "✊", 
        "🤛", "🤜", "🤲", "👐", "🙌", "🫶", "👎", "👍", "👏", "💪", 
        "👃", "👂", "🦻", "🦶", "🦵"
    )
      
    companion object {
        const val WIDGET_CLICK_ACTION = "dev.kelvinwilliams.PRE.WIDGET_CLICK" 
    }
    
    // --- PRIVATE UTILITY METHOD ---
    private fun getShuffledEmoticon(): String {
        val tempArray = EMOTICONS.toMutableList()
        
        val numShuffles = Random.nextInt(1, 6) 

        repeat(numShuffles) {
            tempArray.shuffle()
        }
        
        val randomIndex = Random.nextInt(tempArray.size)
        return tempArray[randomIndex]
    } // <--- CORRECT CLOSING BRACE PLACED HERE

    // --- WIDGET UPDATE METHOD ---
    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // val currentEmoticon = getShuffledEmoticon()
        val currentEmoticon = EMOTICONS.random()
        views.setTextViewText(R.id.emoticon_text_view, currentEmoticon)

        val intent = Intent(context, EmoticonAppWidget::class.java).apply {
            action = WIDGET_CLICK_ACTION
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
    } // <--- CORRECT CLOSING BRACE PLACED HERE

    // --- APPWIDGETPROVIDER OVERRIDES ---

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (WIDGET_CLICK_ACTION == intent.action) {
            Log.d("Widget", "Emoticon widget clicked!")
            
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                intent.component
            )
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }
}
