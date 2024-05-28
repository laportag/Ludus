package com.doorxii.ludus.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.DatabaseManagement.returnDb
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LudusManagementActivity() : ComponentActivity() {

    lateinit var db: AppDatabase
    lateinit var repository: LudusRepository
    var ludus: Ludus? = null
    val viewModel: LudusManagementActivityViewModel by viewModels()


    var ludusName = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbName = intent.getStringExtra("db_name")
        Log.d(TAG, "onCreate db name: $dbName")
    // null pointer exception, change intent extras
        db = returnDb(dbName!!, applicationContext)
        repository = LudusRepository(db)

        viewModel.setRespository(repository)
        GlobalScope.launch (Dispatchers.IO) {
            val player = repository.ludusDao.getPlayerLudus().first()
            Log.d(TAG, "onCreate: ${player.name}")
            ludus = player
        }



        enableEdgeToEdge()
        setContent {
            LudusTheme {
                LudusManagementLayout()
            }
        }

    }


    @Composable
    fun LudusManagementLayout() {

        if (ludus != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ludus: ${ludus?.name}")
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