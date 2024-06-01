import androidx.compose.ui.window.ComposeUIViewController
import pm.bam.mbc.common.theme.MonkeyTheme
import pm.bam.mbc.navigation.NavGraph

fun MainViewController() = ComposeUIViewController {
    MonkeyTheme {
        NavGraph()
    }
}