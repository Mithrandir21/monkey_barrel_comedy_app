package pm.bam.mbc.feature.webview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pm.bam.mbc.feature.webview.ui.WebviewScreen

fun NavGraphBuilder.webviewScreen(
    navController: NavController,
    route: String,
    titleArg: String,
    urlArg: String,
) {
    composable(
        route = route,
        arguments = listOf(navArgument(titleArg) { type = NavType.StringType }, navArgument(urlArg) { type = NavType.StringType })
    ) { entry ->
        WebviewScreen(
            title = entry.arguments?.getString(titleArg)!!,
            url = entry.arguments?.getString(urlArg)!!,
            onBack = { navController.popBackStack() },
        )
    }
}