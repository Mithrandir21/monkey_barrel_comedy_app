package pm.bam.mbc.feature.webview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pm.bam.mbc.feature.webview.ui.WebviewScreen

fun NavGraphBuilder.webviewScreen(
    navController: NavController,
    route: String
) {
    composable(route) {
        WebviewScreen()
    }
}