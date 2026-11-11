package com.waymate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.waymate.app.di.WayMateAppLocator
import com.waymate.app.ui.navigation.WayMateNavGraph
import com.waymate.app.ui.theme.WayMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Application Container & Mock Repositories
        WayMateAppLocator.initialize()

        enableEdgeToEdge()
        setContent {
            WayMateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WayMateNavGraph()
                }
            }
        }
    }
}
