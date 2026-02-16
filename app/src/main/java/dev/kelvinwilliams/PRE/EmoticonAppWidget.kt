package dev.kelvinwilliams.PRE

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlin.random.Random

class EmoticonAppWidget : AppWidgetProvider() {

    // FIX 1: Explicitly define the type so it isn't inferred as 'Nothing?'
    private var emoList: MutableList<String>? = null

    companion object {
        const val WIDGET_CLICK_ACTION = "dev.kelvinwilliams.PRE.WIDGET_CLICK" 
    }

    private fun getShuffledEmoticon(context: Context): String {
        // FIX 2: Use a local variable to allow for 'Smart Casting'
        var currentList = emoList
        
        if (currentList == null) {
            val emoString = context.getString(R.string.emo_string)
            val newList = emoString.codePoints().toArray()
                .map { String(Character.toChars(it)) }
                .toMutableList()
            
            emoList = newList
            currentList = newList
        }

        // At this point, currentList is guaranteed to be non-null
        // We create a temporary copy to shuffle so the original cache remains stable
        val temp = currentList.toMutableList()
        val numShuffles = Random.nextInt(2, 6) 
        repeat(numShuffles) {
            temp.shuffle()
        }
        
        return temp.randomOrNull() ?: "?"
    }

    // --- WIDGET UPDATE METHOD ---
    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        
        

        val mic = Random.nextLong()
        val target = 777L
        val currentEmoticon = getShuffledEmoticon(context)

        if (mic == target) {
            views.setTextViewText(R.id.emoticon_text_view, currentEmoticon)
            views.setTextViewText(R.id.mic_text_view, "♾️")
        } else {
            views.setTextViewText(R.id.emoticon_text_view, "")
            views.setTextViewText(R.id.mic_text_view, currentEmoticon)
        } 

        val intent = Intent(context, EmoticonAppWidget::class.java).apply {
            action = WIDGET_CLICK_ACTION
            data = android.net.Uri.parse("widget://$appWidgetId")
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
            val componentName = android.content.ComponentName(context, EmoticonAppWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}
