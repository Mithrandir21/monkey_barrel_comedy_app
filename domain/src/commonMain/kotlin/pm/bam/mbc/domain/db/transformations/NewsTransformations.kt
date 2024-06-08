package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.remote.models.RemoteArtist
import pm.bam.mbc.remote.models.RemoteNews
import pmbammbcdomain.DatabaseArtist
import pmbammbcdomain.DatabaseNews


internal fun RemoteNews.toDatabaseNews(serializer: Serializer): DatabaseNews = DatabaseNews(
    id = id,
    title = title,
    description = description,
    images = serializer.serialize(images),
    types = serializer.serialize(types),
    showsIds = showsIds?.let { serializer.serialize(it) },
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
    podcastsIds = podcastsIds?.let { serializer.deserialize(it) },
    blogPostsIds = blogPostsIds?.let { serializer.deserialize(it) },
    externalLinks = externalLinks?.let { serializer.deserialize(it) }
)