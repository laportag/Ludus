package com.doorxii.ludus.ui.components.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun <T> DropTarget(
    modifier: Modifier,
    isTargetlessDropTarget: Boolean = false,
    content: @Composable (BoxScope.(isInBound: Boolean, data: T?) -> Unit)
) {

    val dragInfo = DragTargetInfo.LocalDragTargetInfo.current
    val dragPosition = dragInfo.dragPosition
    val dragOffset = dragInfo.dragOffset
    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier.onGloballyPositioned {
        it.boundsInWindow().let { rect ->
            val isTargetedCard = dragInfo.combatActionType is CombatActionType.Targeted
            val isTargetlessCard = dragInfo.combatActionType is CombatActionType.Targetless
            isCurrentDropTarget = when {
                isTargetedCard && !isTargetlessDropTarget -> rect.contains(dragPosition + dragOffset) // Targeted card on a targeted drop target
                isTargetlessCard && isTargetlessDropTarget -> rect.contains(dragPosition + dragOffset) // Targetless card on a targetless drop target
                else -> false
            }
        }
    }) {
        val data =
            if (isCurrentDropTarget && !dragInfo.isDragging) dragInfo.dataToDrop as T? else null
        content(isCurrentDropTarget, data)
    }
}