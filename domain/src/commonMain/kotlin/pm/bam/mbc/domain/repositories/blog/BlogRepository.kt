package pm.bam.mbc.domain.repositories.blog

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.BlogPost

interface BlogRepository {

    fun observeBlogPosts(): Flow<List<BlogPost>>

    fun getBlogPost(blogPostId: Long): BlogPost

    fun refreshBlogPosts()
}