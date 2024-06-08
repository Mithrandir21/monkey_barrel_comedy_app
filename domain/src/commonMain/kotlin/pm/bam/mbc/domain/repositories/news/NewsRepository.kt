package pm.bam.mbc.domain.repositories.news

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.News

interface NewsRepository {

    fun observeNews(): Flow<List<News>>

    fun getNews(newsId: Long): News

    fun getNews(vararg newsId: Long): List<News>

    fun refreshNews()

}