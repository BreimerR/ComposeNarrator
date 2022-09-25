package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.TransformOrigin

val defaultAnimationDuration = 350

val defaultEntryAnimation = fadeIn(animationSpec = tween(defaultAnimationDuration)) + slideInVertically(
    animationSpec = tween(defaultAnimationDuration)
) { it }

val defaultExitAnimation = fadeOut(animationSpec = tween(defaultAnimationDuration)) + slideOutVertically(
    animationSpec = tween(defaultAnimationDuration)
) { -it }