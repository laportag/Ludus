package com.doorxii.ludus.ui.components.cards

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun <T> DragTarget(
    modifier: Modifier,
    dataToDrop: T,
    combatActionType: CombatActionType,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {

    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = DragTargetInfo.LocalDragTargetInfo.current

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
        }
        .pointerInput(Unit) {
            if (enabled) {
                detectDragGesturesAfterLongPress(onDragStart = {
                    currentState.dataToDrop = dataToDrop
                    currentState.isDragging = true
                    currentState.dragPosition = currentPosition + it
                    currentState.draggableComposable = { content() }
                    currentState.combatActionType = combatActionType
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                }, onDragEnd = {
                    currentState.isDragging = false
                    currentState.dragOffset = Offset.Zero
                }, onDragCancel = {
                    currentState.dragOffset = Offset.Zero
                    currentState.isDragging = false
                })
            }
        }) {
        content()
    }
}