package pm.bam.mbc.domain.repositories.blog

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toBlogPost
import pm.bam.mbc.domain.db.transformations.toDatabaseBlogPost
import pm.bam.mbc.domain.models.BlogPost
import pm.bam.mbc.remote.datasources.RemoteBlogDataSource
import pmbammbcdomain.DatabaseBlogPostQueries

internal class BlogRepositoryImpl(
    private val serializer: Serializer,
    private val remoteBlogDataSource: RemoteBlogDataSource,
    private val blogPostQueries: DatabaseBlogPostQueries
) : BlogRepository {

    override fun observeBlogPosts(): Flow<List<BlogPost>> =
        blogPostQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { blogPosts -> blogPosts.map { it.toBlogPost(serializer) } }

    override fun getBlogPost(blogPostId: Long): BlogPost =
        blogPostQueries.selectById(blogPostId.toLong())
            .executeAsOne()
            .toBlogPost(serializer)

    override fun refreshBlogPosts() =
        remoteBlogDataSource.getAllBlogPosts()
            .map { it.toDatabaseBlogPost(serializer) }
            .toList()
            .let { blogPosts ->
                blogPostQueries.transaction {
                    blogPostQueries.deleteAll()
                    blogPosts.forEach { blogPostQueries.insert(it) }
                }
            }
}