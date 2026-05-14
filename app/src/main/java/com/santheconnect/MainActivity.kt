package com.santheconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.santheconnect.ui.theme.SantheConnectTheme
import com.santheconnect.ui.SantheConnectApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        println("Firebase working: $auth")
        enableEdgeToEdge()
        setContent {
            SantheConnectTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SantheConnectApp()
                }
            }
        }
    }
}
