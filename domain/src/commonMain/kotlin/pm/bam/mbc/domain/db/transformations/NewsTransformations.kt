package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.models.toLink
import pm.bam.mbc.domain.models.toNewsType
import pm.bam.mbc.remote.models.RemoteNews
import pm.bam.mbc.remote.models.mapIds
import pmbammbcdomain.DatabaseNews

internal fun RemoteNews.toNews(): News = News(
    id = id,
    title = title,
    description = description,
    images = images,
    types = types.map { it.toNewsType() },
    showsIds = showIds?.mapIds(),
    merchIds = merchIds?.mapIds(),
    podcastsIds = episodeIds?.mapIds(),
    blogPostsIds = blogPostsIds?.mapIds(),
    externalLinks = externalLinks?.map { it.toLink() }
)

internal fun News.toDatabaseNews(serializer: Serializer): DatabaseNews = DatabaseNews(
    id = id,
    title = title,
    description = description,
    images = serializer.serialize(images),
    types = serializer.serialize(types),
    showsIds = showsIds?.let { serializer.serialize(it) },
    merchIds = merchIds?.let { serializer.serialize(it) },
    podcastsIds = podcastsIds?.let { serializer.serialize(it) },
    blogPostsIds = blogPostsIds?.let { serializer.serialize(it) },
    externalLinks = externalLinks?.let { serializer.serialize(it) }
)

internal fun DatabaseNews.toNews(serializer: Serializer): News = News(
    id = id,
    title = title,
    description = description,
    images = serializer.deserialize(images),
    types = serializer.deserialize(types),
    showsIds = showsIds?.let { serializer.deserialize(it) },
    merchIds = merchIds?.let { serializer.deserialize(it) },
    podcastsIds = podcastsIds?.let { serializer.deserialize(it) },
    blogPostsIds = blogPostsIds?.let { serializer.deserialize(it) },
    externalLinks = externalLinks?.let { serializer.deserialize(it) }
)