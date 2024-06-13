package pm.bam.mbc.domain.repositories.news

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toDatabaseNews
import pm.bam.mbc.domain.db.transformations.toNews
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.remote.datasources.RemoteNewsDataSource
import pmbammbcdomain.DatabaseNewsQueries

internal class NewsRepositoryImpl(
    private val serializer: Serializer,
    private val remoteNewsDataSource: RemoteNewsDataSource,
    private val newsQueries: DatabaseNewsQueries
) : NewsRepository {

    override fun observeNews(): Flow<List<News>> =
        newsQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseNews -> databaseNews.map { it.toNews(serializer) } }

    override fun getNews(newsId: Long): News =
        newsQueries.selectById(newsId)
            .executeAsOne()
            .toNews(serializer)

    override fun getNews(vararg newsId: Long): List<News> =
        newsQueries.selectByIds(newsId.toList())
            .executeAsList()
            .map { it.toNews(serializer) }

    override suspend fun refreshNews() =
        remoteNewsDataSource.getAllNews()
            .map { it.toDatabaseNews(serializer) }
            .toList()
            .let { news ->
                newsQueries.transaction {
                    newsQueries.deleteAll()
                    news.forEach { newsQueries.insert(it) }
                }
            }
}