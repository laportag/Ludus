package com.doorxii.ludus.ui.components.cards

sealed class CombatActionType {
    object Targeted : CombatActionType()
    object Targetless : CombatActionType()
}