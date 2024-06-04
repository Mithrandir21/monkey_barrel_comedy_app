import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.di.appModule
import pm.bam.mbc.navigation.NavGraph

fun main() = application {
    startKoin {
        modules(appModule())
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "MonkeyBarrelComedy",
    ) {
        MonkeyTheme {
            NavGraph()
        }
    }
}