package dev.kelvinwilliams.PRE

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This will display a simple layout (activity_main.xml) when the app icon is tapped.
        setContentView(R.layout.activity_main) 
    }
}
