package com.doorxii.ludus.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.ui.components.LoadLudusDialogue
import com.doorxii.ludus.ui.components.NewLudusDialogue
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.DatabaseManagement.getAllDatabases
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {

    private lateinit var viewModel: StartActivityViewModel

    private var databases = mutableStateOf(listOf<String>())

    private var newDialogueVisible = false
    private var loadDialogueVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[StartActivityViewModel::class.java]
        observeFlows()
        viewModel.setDatabases(getAllDatabases(applicationContext))


        enableEdgeToEdge()
        setContent {
            LudusTheme {
                StartMenu()
            }
        }

        Log.d(TAG, "dbs: ${getAllDatabases(applicationContext)}")
    }

    private fun observeFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.databases.collect {
                    Log.d(TAG, "dbs: $it")
                    databases.value = it
                }
            }
        }
    }

    private fun launchLudusManagement(name: String, db: AppDatabase) {
        val intent = Intent(this, LudusManagementActivity::class.java)
        intent.putExtra("db_name", name)
        startActivity(intent)
    }

    @Preview
    @Composable
    fun StartMenu() {

        val newDialogueState = remember { mutableStateOf(newDialogueVisible) }
        val loadDialogueState = remember { mutableStateOf(loadDialogueVisible) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { newDialogueState.value = true }) {
                Text("New Ludus")
            }
            Button(onClick = { loadDialogueState.value = true }) {
                Text("Load Ludus")
            }

        }
        if (newDialogueState.value) {
            NewLudusDialogue(
                databases = databases.value,
                visibleCallback = { newDialogueState.value = it },
                launchLudusManagement = { ludus, db ->
                    launchLudusManagement(ludus, db)
                }
            )
        }
        if (loadDialogueState.value) {

            LoadLudusDialogue(
                databasesList = databases.value,
                viewModel = viewModel,
                visibleCallback = { loadDialogueState.value = it },
                launchLudusManagement = { ludus, db ->
                    launchLudusManagement(ludus, db)
                }
            )
        }
    }

    companion object {
        private const val TAG = "StartActivity"
    }

}