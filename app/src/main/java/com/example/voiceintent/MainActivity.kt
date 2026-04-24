package com.example.voiceintent

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.voiceintent.feature.record.presentation.service.RecordControl
import com.example.voiceintent.feature.record.presentation.service.RecordService
import com.example.voiceintent.feature.record.presentation.service.RecordServiceConnection
import com.example.voiceintent.navigation.AppNavHost
import com.example.voiceintent.navigation.BottomNavItem
import com.example.voiceintent.navigation.Screen
import com.example.voiceintent.shared.ui.theme.VoiceIntentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var recordControl by mutableStateOf<RecordControl?>(null)

    private val recordServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName?,
            binder: IBinder?
        ) {
            val service = (binder as RecordService.LocalBinder).getService()
            recordControl = RecordServiceConnection(service = service)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            recordControl = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeRecordService()

        enableEdgeToEdge()
        setContent {
            VoiceIntentTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    Screen.Notes.route,
                    Screen.Settings.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                BottomNavItem.items.forEach { item ->
                                    NavigationBarItem(
                                        selected = currentRoute == item.screen.route,
                                        onClick = {
                                            navController.navigate(item.screen.route) {
                                                popUpTo(Screen.Notes.route) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(item.iconRes),
                                                contentDescription = item.label
                                            )
                                        },
                                        label = { Text(item.label) }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        recordControl = recordControl,
                        modifier = Modifier
                            .padding(
                                paddingValues = PaddingValues(
                                    bottom = innerPadding.calculateBottomPadding()
                                )
                            )
                    )
                }
            }
        }
    }

    private fun initializeRecordService() {
        Intent(this, RecordService::class.java).also { intent ->
            bindService(intent, recordServiceConnection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(recordServiceConnection)
        recordControl = null
    }
}