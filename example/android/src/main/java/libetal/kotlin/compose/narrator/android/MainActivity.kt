package libetal.kotlin.compose.narrator.android

import libetal.kotlin.compose.narrator.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import libetal.kotlin.compose.narrator.StoryScope

class MainActivity : AppCompatActivity() {

    lateinit var narrationScope: StoryScope<*, *>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App({
                    narrationScope = this
                }) {
                    onBackPressed()
                }
            }
        }

        lifecycleScope
    }

    override fun onBackPressed() {
        if (narrationScope.back()) super.onBackPressed()
    }

}

@Preview
@Composable
fun AppPreview() {
    App({}) {
        println("Close app requested")
        true
    }
}