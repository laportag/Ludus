package com.doorxii.ludus.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.DatabaseManagement.returnDb
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LudusManagementActivity() : ComponentActivity() {

    lateinit var db: AppDatabase
    lateinit var repository: LudusRepository
    var ludus = mutableStateOf<Ludus?>(null)
    lateinit var viewModel: LudusManagementActivityViewModel

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbName = intent.getStringExtra("db_name")
        Log.d(TAG, "onCreate db name: $dbName")

        db = returnDb(dbName!!, applicationContext)
        repository = LudusRepository(db)

        viewModel = ViewModelProvider(this)[LudusManagementActivityViewModel::class.java]
        viewModel.start(repository)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerLudus.collect { playerLudus ->
                    Log.d(TAG, "onCreate playerLudus: $playerLudus")
                    ludus.value = playerLudus
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            LudusTheme {
                LudusManagementLayout()
            }
        }

    }

    fun launchBarracksManagement() {

    }

    fun launchGladiatorMarket() {

    }

    fun chooseEnemyLudus() {

    }

    @Composable
    fun BarracksManagement(){

    }



    @Preview
    @Composable
    fun LudusManagementLayout() {

        if (ludus.value != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ludus: ${ludus.value?.name}")
                Button(onClick = { launchBarracksManagement() }) {
                    Text("Manage Barracks")
                }
                Button(onClick = { launchGladiatorMarket() }) {
                    Text("Purchase Gladiators")
                }
                Row {
                    Button(onClick = { chooseEnemyLudus() }) {
                        Text("Choose Enemy Ludus")
                    }
                }
            }
        }


    }

    override fun finishActivity(requestCode: Int) {
        super.finishActivity(requestCode)
    }

    companion object {
        const val TAG = "LudusManagementActivity"
    }


}