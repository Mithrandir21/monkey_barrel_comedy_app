import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.startKoin
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.di.appModule
import pm.bam.mbc.navigation.NavGraph
import java.io.File

fun main() = application {
    startKoin {
        modules(appModule())
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "MonkeyBarrelComedy",
    ) {
        webViewSetup {
            MonkeyTheme {
                NavGraph()
            }
        }
    }
}

@Composable
private fun webViewSetup(
    content: @Composable () -> Unit
) {
    var restartRequired by remember { mutableStateOf(false) }
    var downloading by remember { mutableStateOf(0F) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(builder = {
                installDir(File("kcef-bundle"))
                progress {
                    onDownloading {
                        downloading = it
                    }
                    onInitialized {
                        initialized = true
                    }
                }
                release("jbr-release-17.0.10b1087.23")
                settings {
                    cachePath = File("cache").absolutePath
                }
            }, onError = {
                it?.printStackTrace()
            }, onRestartRequired = {
                restartRequired = true
            })
        }
    }

    if (restartRequired) {
        Text(text = "Restart required.")
    } else {
        if (initialized) {
            content()
        } else {
            Text(text = "Downloading $downloading%")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            KCEF.disposeBlocking()
        }
    }
}