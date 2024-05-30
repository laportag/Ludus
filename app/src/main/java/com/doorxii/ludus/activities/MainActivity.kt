package com.doorxii.ludus.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.GladiatorDao
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.CombatSerialization.returnCombatFile
import com.doorxii.ludus.utils.CombatSerialization.saveCombatJson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File

class MainActivity : androidx.activity.ComponentActivity() {

    private var combat: Combat? = null
    private var gladiatorList = mutableListOf<Gladiator>()

    private var isStartGameUIEnabled: Boolean = true

    private lateinit var combatFile: File

    private var text = mutableStateOf("TEST TEXT")

    private val combatResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "Combat result: $result")
                val resultUri: Uri = result.data?.data!!
                val resFile = File(resultUri.path!!)
                val resCombat = Json.decodeFromString<Combat>(resFile.readText())
                combatFinished(resCombat)
            }
        }

    @OptIn(DelicateCoroutinesApi::class)
    fun combatFinished(resCombat: Combat) {
        Log.d(TAG, "Combat complete?: ${resCombat.isComplete}")
        if (resCombat.gladiatorList.isEmpty()) {
            text.value = "No winner"
            Log.d(TAG, "No winner")
            return
        }
        val winner = resCombat.gladiatorList[0]
        Log.d(TAG, "Winner: ${winner.name}")
        text.value = "Winner: ${winner.name}"

        GlobalScope.launch(Dispatchers.IO) {
            for (gladiator in resCombat.originalGladiatorList) {
                if (gladiator !in resCombat.gladiatorList) {
                    // dead gladiators
                    gladiator.health = 0.0
                    gladiatorDao.updateGladiator(gladiator)
                }
            }
            for (gladiator in resCombat.gladiatorList) {
                gladiatorDao.updateGladiator(gladiator)
            }
            updateLudusList()
        }
    }

    private var choiceA = mutableStateOf(Gladiator())
    private var choiceB = mutableStateOf(Gladiator())


    private fun startCombatActivity(gladiatorList: List<Gladiator>) {
        combatFile = returnCombatFile(applicationContext)
        combat = Combat.init(gladiatorList)
        saveCombatJson(combat!!, combatFile)
        Log.d(TAG, "combatfile: " + combatFile.readText())
        val uri = combatFile.toUri()
        val intent = Intent(this, CombatActivity::class.java)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.putExtra("combatFileUri", uri)
        intent.setDataAndType(uri, contentResolver.getType(uri))
        combatResultLauncher.launch(intent)
        startActivity(intent)
    }

    private fun simCombat(gladiatorList: List<Gladiator>) {
        combat = Combat.init(gladiatorList)
        val res = combat!!.simCombat()
        text.value = "Sim: " + res.combatReport
        Log.d(TAG, "res: " + res.combatReport)
        combatFinished(combat!!)
    }

    lateinit var db: AppDatabase
    private lateinit var gladiatorDao: GladiatorDao

    private var romeList = mutableListOf<Gladiator>()
    private var capuaList = mutableListOf<Gladiator>()


    private fun initDb() {
//
//        GlobalScope.launch(Dispatchers.IO) {
//            db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java,
//                "ludus.db"
//            )
//                .build()
//
//            ludusDao = db.ludusDao()
//            gladiatorDao = db.gladiatorDao()
//
//            val romeLudus: Ludus = Ludus("Rome")
//            val capuaLudus: Ludus = Ludus("Capua")
//
//            val listA = newGladiatorList(5)
//            val listB = newGladiatorList(5)
//
//
////            ludusDao.insertLudus(romeLudus)
////            ludusDao.insertLudus(capuaLudus)
//
//            rome = ludusDao.getLudusByName("Rome")
//            capua = ludusDao.getLudusByName("Capua")
//            Log.d(TAG, "id rome: ${rome.ludusId}, id capua: ${capua.ludusId}")
//            updateLudusList()
//            romeList = gladiatorDao.getByLudusId(rome.ludusId).toMutableList()
//            capuaList = gladiatorDao.getByLudusId(capua.ludusId).toMutableList()

//            listA.forEach { gladiator ->
//                try {
//                    gladiator.ludusId = rome.ludusId
//                    gladiatorDao.insertGladiator(gladiator)
//                } catch (e: Exception) {
//                    Log.d(TAG, "error: ${e.message}")
//                }
//
//            }
//            listB.forEach { gladiator ->
//                try {
//                    gladiator.ludusId = capua.ludusId
//                    gladiatorDao.insertGladiator(gladiator)
//                } catch (e: Exception) {
//                    Log.d(TAG, "error: ${e.message}")
//                }
//            }

//            val all = ludusDao.getAllLudus()
////            Log.d(TAG, "rome: $rome, capua: $capua")
//
////            val ostiaLu = Ludus("Ostia")
////            ludusDao.insertLudus(ostiaLu)
//
//            var romelist = gladiatorDao.getByLudusId(rome.ludusId)
//            var romestr = ""
//            romelist.forEach {
//                romestr += Json.encodeToString(it)
//            }
//            Log.d(TAG, "rome: ${romestr}")
//
//            var capualist = gladiatorDao.getByLudusId(capua.ludusId)
//            var capuastr = ""
//            capualist.forEach {
//                capuastr += Json.encodeToString(it)
//            }
//            Log.d(TAG, "capua: ${capuastr}")
//
//            Log.d(TAG, "all: ${Json.encodeToString(all)}")
//            Log.d(TAG, "get all glads: ${(gladiatorDao.getAll())}")
//            Log.d(TAG, "get rome glads: ${(gladiatorDao.getByLudusId(rome.ludusId))}")
//            Log.d(TAG, "get capua glads: ${(gladiatorDao.getByLudusId(capua.ludusId))}")
//        }
    }

    private fun updateLudusList() {
//        GlobalScope.launch(Dispatchers.IO) {
//            romeList = mutableListOf()
//            for (gladiator in gladiatorDao.getByLudusId(rome.ludusId).toMutableList()) {
//                if (gladiator.isAlive()) {
//                    romeList.add(gladiator)
//                }
//            }
//            capuaList = mutableListOf()
//            for (gladiator in gladiatorDao.getByLudusId(capua.ludusId).toMutableList()) {
//                if (gladiator.isAlive()) {
//                    capuaList.add(gladiator)
//                }
//            }
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDb()
//        testLudus()
//        setGladiators()

        combatFile = returnCombatFile(context = applicationContext)

        enableEdgeToEdge()
        setContent {

            LudusTheme {
                HomeScreen()
            }
        }
    }

    @Preview
    @Composable
//    @Preview
    fun HomeScreen() {
        val text by remember { mutableStateOf(text) }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            TopAppBar(title = { Text("Ludus") })
            Text("Combat: ${choiceA.value.name} v ${choiceB.value.name}")
            Row {
                Button(
                    onClick = {
                        gladiatorList = mutableListOf(choiceA.value, choiceB.value)
                        startCombatActivity(gladiatorList)
                    },
                    enabled = isStartGameUIEnabled
                ) {
                    Text("Start")
                }
                Button(
                    onClick = {
                        gladiatorList = mutableListOf(choiceA.value, choiceB.value)
                        simCombat(gladiatorList)
                    },
                    enabled = isStartGameUIEnabled
                ) {
                    Text("Sim")
                }
            }

            Row(
                Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.weight(1f)
                ) {
                    BarracksList(list = romeList) {
                        choiceA.value = it
                    }
                }
                Column(
                    Modifier.weight(1f)
                ) {
                    BarracksList(list = capuaList) {
                        choiceB.value = it
                    }
                }
            }
            TextField(
                value = text.value,
                onValueChange = {},
                minLines = 15,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            )
        }
    }

    @Composable
    fun BarracksList(
        list: List<Gladiator>,
        onItemSelected: (Gladiator) -> Unit
    ) {
        LazyColumn {
            items(list) { gladiator ->
                SelectableItem(
                    gladiator,
                    onItemSelected
                )

            }
        }
    }

    @Composable
    fun SelectableItem(
        item: Gladiator,
        onSelected: (Gladiator) -> Unit
    ) {
        val isSelected = item == choiceA.value || item == choiceB.value
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelected(item) },
            shape = MaterialTheme.shapes.small,
            tonalElevation = 4.dp,
            color = if (isSelected) Color.LightGray else Color.Transparent
        ) {
            Text(
                text = item.name,
                modifier = Modifier.padding(16.dp)
            )
        }
    }


    companion object {
        private const val TAG = "MainActivity"
    }

}
