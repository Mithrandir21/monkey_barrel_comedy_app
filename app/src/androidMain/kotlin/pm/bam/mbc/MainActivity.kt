package pm.bam.mbc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            MonkeyTheme {
                NavGraph()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MonkeyTheme {
        NavGraph()
    }
}