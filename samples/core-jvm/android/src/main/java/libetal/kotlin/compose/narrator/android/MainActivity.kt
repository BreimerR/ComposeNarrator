package libetal.kotlin.compose.narrator.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import libetal.kotlin.compose.narrator.*
import libetal.kotlin.compose.narrator.common.App
import libetal.kotlin.compose.narrator.common.AppNarrations

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                App()
            }
        }


    }

  /*  override fun onBackPressed() {
        narrationScope.back()
    }*/

    companion object {
        const val TAG = "MainActivity"
    }
}
