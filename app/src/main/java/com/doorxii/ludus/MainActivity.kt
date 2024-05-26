package com.doorxii.ludus

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.room.Room
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.armour.Armours
import com.doorxii.ludus.data.models.equipment.armour.LightArmour
import com.doorxii.ludus.data.models.equipment.weapon.Gladius
import com.doorxii.ludus.data.models.equipment.weapon.Weapons
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.CombatSerialization.returnCombatFile
import com.doorxii.ludus.utils.CombatSerialization.saveCombatJson
import com.doorxii.ludus.utils.GladiatorGenerator.newGladiator
import com.doorxii.ludus.utils.GladiatorGenerator.newGladiatorList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class MainActivity : androidx.activity.ComponentActivity() {

    private var combat: Combat? = null
    var gladiatorList = mutableListOf<Gladiator>()

    private var isStartGameUIEnabled: Boolean = true
    private var isActionUIEnabled: Boolean = false

    lateinit var combatFile: File

    var text = mutableStateOf("TEST TEXT")
//    var titus = Gladiator()
//    var joseph = Gladiator()

    val combatResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "Combat result: ${result.toString()}")
                val resultUri: Uri = result.data?.data!!
                val resFile = File(resultUri.path!!)
                val resCombat = Json.decodeFromString<Combat>(resFile.readText())
                Log.d(TAG, "Combat complete?: ${resCombat.isComplete.toString()}")
                if (resCombat.gladiatorList.size == 0){
                    text.value = "No winner"
                    Log.d(TAG, "No winner")
                    return@registerForActivityResult
                }
                val winner = resCombat.gladiatorList[0]
                Log.d(TAG, "Winner: ${winner.name}")
                text.value = "Winner: ${winner.name}"
            }
        }

    var listA: MutableList<Gladiator> = mutableListOf()
    var listB: MutableList<Gladiator> = mutableListOf()
    var choiceA = mutableStateOf<Gladiator>(Gladiator())
    var choiceB = mutableStateOf<Gladiator>(Gladiator())

//    fun testLudus() {
//        val romeLudus: Ludus = Ludus.init(
//            "Rome Ludus",
//            newGladiatorList(5)
//        )
//        listA = romeLudus.barracks  //.gladiators
//        val capuaLudus: Ludus = Ludus.init(
//            "Capua Ludus",
//            newGladiatorList(5)
//        )
//        listB = capuaLudus.barracks  //.gladiators
//        choiceA.value = listA[0]
//        choiceB.value = listB[0]
//        Log.d(TAG, "rome: " + Json.encodeToString(romeLudus))
//        Log.d(TAG, "capua: " + Json.encodeToString(capuaLudus))
//    }


    fun startCombatActivity(gladiatorList: List<Gladiator>) {
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

    lateinit var db: AppDatabase
    @OptIn(DelicateCoroutinesApi::class)
    fun initDb(){

        GlobalScope.launch(Dispatchers.IO) {
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "database-name"
            )
                .build()

            val ludusDao = db.ludusDao()
            val gladiatorDao = db.gladiatorDao()


//            val listA = newGladiatorList(5)
//            val listB = newGladiatorList(5)
            val romeLudus: Ludus = Ludus("Rome")
            val capuaLudus: Ludus = Ludus("Capua")

//            ludusDao.insertLudus(romeLudus)
//            ludusDao.insertLudus(capuaLudus)

            val rome = ludusDao.getLudusByName("Rome")
            val capua = ludusDao.getLudusByName("Capua")

            val all = ludusDao.getAllLudus()

            Log.d(TAG, "rome: $rome, capua: $capua")

            val ostiaLu = Ludus("Ostia")
//            ludusDao.insertLudus(ostiaLu)


            Log.d(TAG, "all: ${Json.encodeToString(all)}")

        }



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

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
//    @Preview
    fun HomeScreen() {
        var text by remember { mutableStateOf(text) }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            TopAppBar(title = { Text("Ludus") })
            Text("Combat: ${choiceA.value.name} v ${choiceB.value.name}")
            Button(
                onClick = {
                    gladiatorList = mutableListOf(choiceA.value, choiceB.value)
                    startCombatActivity(gladiatorList)
                },
                enabled = isStartGameUIEnabled
            ) {
                Text("Start")
            }
            Row(
                Modifier.fillMaxWidth()
            ){
                Column(
                    Modifier.weight(1f)
                ) {
                    BarracksList(list = listA) {
                        choiceA.value = it
                    }
                }
                Column(
                    Modifier.weight(1f)
                ) {
                    BarracksList(list = listB) {
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
