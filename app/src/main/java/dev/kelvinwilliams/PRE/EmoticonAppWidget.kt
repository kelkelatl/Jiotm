package dev.kelvinwilliams.PRE

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlin.random.Random // Required import for Random class

class EmoticonAppWidget : AppWidgetProvider() {

	private var emoList: MutableList<String>? = null

	
    // --- CLASS PROPERTIES -
	
	/* private val EMOTICONS = arrayOf(
        "🙏", "🖕", "👇", "👆", "☝️", "👈", "👉", "🫵", "👌", "🤏", 
        "🤌", "🤙", "🫰", "🤞", "✌️", "🤘", "🤟", "🖖", "✋", "🖐️", 
        "🤚", "👋", "🫷", "🫸", "🫲", "🫱", "🫴", "🫳", "👊", "✊", 
        "🤛", "🤜", "🤲", "👐", "🙌", "🫶", "👎", "👍", "👏", "💪", 
        "👃", "👂", "🦻", "🦶", "🦵"
    )
      */
	
    companion object {
        const val WIDGET_CLICK_ACTION = "dev.kelvinwilliams.PRE.WIDGET_CLICK" 
    }

	// --- SHUFFLE LOGIC REINSTATED ---
    private fun getShuffledEmoticon(context: Context): String {
	    var currentList = emoList
		
		if (currentList == null) {
            val emoString = context.getString(R.string.emo_string)
            val newList = emoString.codePoints().toArray()
                .map { String(Character.toChars(it)) }
                .toMutableList()
			emoList = newList
			currentList = newList
			
			val numShuffles = Random.nextInt(2, 6) 
			repeat(numShuffles) {
                currentList.shuffle()
            }
           return currentList.random()
		}

		if (emoList != null) {

            val numShuffles = Random.nextInt(2, 6) 
            repeat(numShuffles) {
                emoList.shuffle()
            }
            return emoList.random()
		}

		return "?"
	}

	private fun getRandomEmoticon(): String {
		/*
        val ranges = listOf(
            0x0023..0x0023,   // Number Sign (#)
            0x002A..0x002A,   // Asterisk (*)
            0x0030..0x0039,   // Digits 0-9
            0x2600..0x26FF,   // Miscellaneous Symbols
            0x2700..0x27BF,   // Dingbats
            0x1F100..0x1F1FF, // Enclosed Alphanumeric Supplement
            0x1F300..0x1F5FF, // Misc Symbols and Pictographs
            0x1F600..0x1F64F, // Emoticons (Smileys)
            0x1F680..0x1F6FF, // Transport and Map Symbols
            0x1F900..0x1F9FF, // Supplemental Symbols and Pictographs
            0x1FA70..0x1FAFF  // Symbols and Pictographs Extended-A
        )

        // Select a random range from the list
        val randomRange = ranges.random()
        
        // Select a random integer value within that range
        val randomValue = kotlin.random.Random.nextInt(randomRange.first, randomRange.last + 1)
		*/
		val oneRange = (0x0023..0x1FAFF).toList()
		val randomizedRange = oneRange.shuffled()
		val random = kotlin.random.Random.nextInt(0, randomizedRange.size + 1)
		val randomValue = randomizedRange[random]
        // Convert the Unicode code point to a String
        return String(Character.toChars(randomValue))
	}
    // --- WIDGET UPDATE METHOD ---
    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        views.setTextViewText(R.id.emoticon_text_view, "J")

			
        val currentEmoticon = getShuffledEmoticon(context)
		// val currentEmoticon = getRandomEmoticon()
        // val currentEmoticon = EMOTICONS.random()
        views.setTextViewText(R.id.emoticon_text_view, currentEmoticon)

			
	    val mic: Int = kotlin.random.Random.nextInt(0,3)
		views.setTextViewText(R.id.mic_text_view, "❌")
        if (mic == 2) {
               views.setTextViewText(R.id.mic_text_view, "♾️")
		}

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
