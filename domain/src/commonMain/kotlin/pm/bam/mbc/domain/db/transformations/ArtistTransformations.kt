package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.remote.models.RemoteArtist
import pmbammbcdomain.DatabaseArtist


internal fun RemoteArtist.toDatabaseArtist(serializer: Serializer): DatabaseArtist = DatabaseArtist(
    id = id,
    firstname = firstname,
    lastname = lastname,
    description = description,
    images = serializer.serialize(images),
    genres = serializer.serialize(genres),
    showsIds = showsIds?.let { serializer.serialize(it) },
    merchIds = merchIds?.let { serializer.serialize(it) },
    podcastsIds = podcastsEpisodeIds?.let { serializer.serialize(it) },
    blogPostsIds = blogPostsIds?.let { serializer.serialize(it) },
    externalLinks = externalLinks?.let { serializer.serialize(it) }
)

internal fun DatabaseArtist.toArtist(serializer: Serializer): Artist = Artist(
    id = id,
    firstname = firstname,
    lastname = lastname,
    description = description,
    images = serializer.deserialize(images),
    genres = serializer.deserialize(genres),
    showsIds = showsIds?.let { serializer.deserialize(it) },
    merchIds = merchIds?.let { serializer.deserialize(it) },
    podcastsIds = podcastsIds?.let { serializer.deserialize(it) },
    blogPostsIds = blogPostsIds?.let { serializer.deserialize(it) },
    externalLinks = externalLinks?.let { serializer.deserialize(it) }
)