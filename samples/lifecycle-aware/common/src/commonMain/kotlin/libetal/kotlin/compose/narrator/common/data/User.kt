package libetal.kotlin.compose.narrator.common.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.reflect.KProperty

data class User(val name: String)


data class UserState(val nameState: MutableState<String?> = mutableStateOf(null)) {

    var name
        get() = nameState.value
        set(value) {
            nameState.value = value?.ifBlank { null }
        }

    val isSet
        get() = name != null


    operator fun getValue(receiver: Any?, property: KProperty<*>) = if (isSet)
        User(
            name = name.orEmpty()
        )
    else null

}
