package pm.bam.mbc.feature.blogs.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pm.bam.mbc.feature.blogs.ui.blog.BlogScreen
import pm.bam.mbc.feature.blogs.ui.post.PostScreen

fun NavGraphBuilder.blogScreen(
    navController: NavController,
    route: String,
    onViewBlogPost: (blogPostId: Long) -> Unit
) {
    composable(route) {
        BlogScreen(
            onBack = { navController.popBackStack() },
            onViewBlogPost = onViewBlogPost
        )
    }
}

fun NavGraphBuilder.blogPostScreen(
    navController: NavController,
    route: String,
    blogPostIdArg: String,
    onViewArtist: (artistId: Long) -> Unit
) {
    composable(
        route = route,
        arguments = listOf(navArgument(blogPostIdArg) { type = NavType.LongType })
    ) { entry ->
        PostScreen(
            blogPostId = entry.arguments?.getLong(blogPostIdArg)!!,
            onBack = { navController.popBackStack() },
            onViewArtist = onViewArtist
        )
    }
}