package libetal.kotlin.compose.narrator.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import libetal.kotlin.compose.narrator.LifeCycleNarrationScope
import libetal.kotlin.compose.narrator.common.App

class MainActivity : AppCompatActivity() {

    lateinit var narrationScope: LifeCycleNarrationScope<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                App()
            }
        }

        lifecycleScope
    }

    override fun onBackPressed() {
        // if (narrationScope.back()) super.onBackPressed()
    }

}
