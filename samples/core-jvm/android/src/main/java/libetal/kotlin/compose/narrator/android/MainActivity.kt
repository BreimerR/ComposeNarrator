package libetal.kotlin.compose.narrator.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.ComposableFun
import libetal.kotlin.compose.narrator.common.App
import libetal.kotlin.compose.narrator.common.AppNarrations
import libetal.kotlin.compose.narrator.interfaces.NarrationScope

class MainActivity : AppCompatActivity() {

    lateinit var narrationScope: NarrationScope<AppNarrations, ComposableFun>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                App {
                    narrationScope = it
                }
            }
        }

    }

    override fun onBackPressed() {
        if (narrationScope.back()) {
            super.onBackPressed()
        }
    }

}
