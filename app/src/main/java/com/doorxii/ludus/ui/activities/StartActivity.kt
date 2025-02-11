package com.doorxii.ludus.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.ui.components.screens.StartActivityScreen
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.DatabaseManagement.getAllDatabases
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {

    private lateinit var viewModel: StartActivityViewModel

    private var databases = mutableStateOf(listOf<String>())

    private var newDialogueVisible = mutableStateOf(false)
    private var loadDialogueVisible = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[StartActivityViewModel::class.java]
        observeFlows()
        viewModel.setDatabases(getAllDatabases(applicationContext))

        enableEdgeToEdge()
        setContent {
            LudusTheme {
                StartActivityScreen(
                    newDialogueState = remember { newDialogueVisible },
                    loadDialogueState = remember { loadDialogueVisible },
                    databases = databases.value,
                    launchLudusManagement = { ludus, db ->
                        launchLudusManagement(ludus, db)
                    },
                    viewModel = viewModel
                )
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

    companion object {
        private const val TAG = "StartActivity"
    }

}