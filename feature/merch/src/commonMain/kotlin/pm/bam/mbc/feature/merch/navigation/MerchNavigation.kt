package pm.bam.mbc.feature.merch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.feature.merch.ui.MerchScreen


fun NavGraphBuilder.merchScreen(
    route: String,
    bottomNavConfig: NavigationBarConfig,
    onViewMerchItem: (merchItemId: Long) -> Unit
) {
    composable(route) {
        MerchScreen(
            bottomNavConfig = bottomNavConfig,
            onViewMerchItem = onViewMerchItem
        )
    }
}
