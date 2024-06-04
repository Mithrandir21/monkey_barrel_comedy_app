import androidx.compose.ui.window.ComposeUIViewController
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.navigation.NavGraph

fun MainViewController() = ComposeUIViewController {
    MonkeyTheme {
        NavGraph()
    }
}