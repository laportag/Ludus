package com.doorxii.ludus.ui.activities

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.components.cards.LongPressDraggable
import com.doorxii.ludus.ui.components.screens.CombatScreen
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.CombatSerialization.readCombatFromFile
import com.doorxii.ludus.utils.CombatSerialization.saveCombatJson
import com.doorxii.ludus.utils.actions.combatactions.ChosenAction
import com.doorxii.ludus.utils.combat.Combat
import com.doorxii.ludus.utils.dice.Dice
import com.doorxii.ludus.utils.dice.DiceTypes
import java.io.File

class CombatActivity : ComponentActivity() {

    private lateinit var viewModel: CombatActivityViewModel

    private var combat = mutableStateOf<Combat?>(null)

    //    private var choice: CombatActions? = null
    private var text = mutableStateOf("")
    private lateinit var combatFile: File

    private var isFinished by mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[CombatActivityViewModel::class.java]
        combat.value = readCombatFromJson()
        Log.d(TAG, "combat null?: " + combat.value!!.combatName)

        enableEdgeToEdge()
        setContent {

            LudusTheme {
                LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                    if (!isFinished){
                        CombatScreen(
                            viewModel,
                            combat,
                            text,
                            ::resetActions,
                            ::makePlayerTurn,
                            ::findNextAvailableGladiator,
                            ::handleSubmissions,
                            ::combatCompleted
                        )
                    }
                }
            }

            LaunchedEffect(combat.value) {
                if (combat.value != null) {
                    resetActions()
                }
            }
        }
    }

    private fun readCombatFromJson(): Combat {
        val uri = intent.data
        combatFile = File(uri!!.path!!)
        val combat = readCombatFromFile(combatFile)
        return combat
    }

    override fun finishActivity(requestCode: Int) {
        Log.d(TAG, "finish combat")
        super.finishActivity(requestCode)
    }

    private fun resetActions(){
        viewModel.resetGladiatorActions() // Reset for new combat
        for (gladiator in combat.value!!.playerGladiatorList) {
            viewModel.updateGladiatorAction(gladiator, null)
        }
    }

    private fun makePlayerTurn(gladiatorActions: List<ChosenAction>) {
        if (gladiatorActions.isNotEmpty()) {
            val roundResult = combat.value!!.playCombatRound(gladiatorActions)
            Log.d(TAG, "roundResult: " + roundResult.combatReport)
            Log.d(TAG, "combat: " + combat.value.toString())
            text.value = roundResult.combatReport
            if (combat.value!!.isComplete) {
                handleSubmissions()
                combatCompleted()
            }
        }
    }

    private fun findNextAvailableGladiator(): Gladiator? {
        return combat.value?.playerGladiatorList?.firstOrNull { !viewModel.hasGladiatorHadATurn(it) }
    }

    private fun handleSubmissions() {
        Log.d(TAG, "handle submissions")
        val allSubmittedGladiators = combat.value!!.submittedEnemyGladiatorList + combat.value!!.submittedPlayerGladiatorList
        allSubmittedGladiators.forEach { gladiator ->
            val listToAddTo = if (gladiator in combat.value!!.originalEnemyGladiatorList) {
                combat.value!!.enemyGladiatorList
            } else {
                combat.value!!.playerGladiatorList
            }

            if (missioGranted(gladiator)) {
                combat.value!!.appendReport("${gladiator.name}: missio")
                listToAddTo.add(gladiator)
            } else {
                combat.value!!.appendReport("${gladiator.name}: sine missio")
            }
        }
    }

    private fun missioGranted(gladiator: Gladiator, ):  Boolean {
        val governorGenerosity = 80.0
        val luck = Dice.totalRolls(Dice.roll(1, DiceTypes.D100))
        var missioScore = 20.0 + luck
        missioScore += gladiator.attack * (0.25 + luck/100)
        missioScore += gladiator.defence * (0.15 + luck/100)
        missioScore -= when {
            gladiator.morale > 80 -> 40.0
            gladiator.morale < 5 -> 20.0
            else -> 10.0
        }
        missioScore += when {
            gladiator.bloodlust >= 90 -> gladiator.bloodlust/10
            gladiator.bloodlust <= 50 -> -(gladiator.bloodlust/10)
            else -> 0.0
        }
        return missioScore >= governorGenerosity
    }

    private fun combatCompleted() {
        Log.d(TAG, "combat completed")
//        val report = combat.value?.combatReport
        saveCombatJson(combat.value!!, combatFile)
        val data = Intent().apply {
            addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(combatFile.toUri(), contentResolver.getType(combatFile.toUri()))
        }
        setResult(RESULT_OK, data)
        isFinished = true
        finish()
        Log.d(TAG, "combat completed finish")
    }

    companion object {
        private const val TAG = "CombatActivity"
    }
}


