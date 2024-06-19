package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.remote.models.RemotePodcast
import pm.bam.mbc.remote.models.RemotePodcastEpisode
import pmbammbcdomain.DatabasePodcast
import pmbammbcdomain.DatabasePodcastEpisode

internal fun RemotePodcast.toDatabasePodcast(serializer: Serializer): DatabasePodcast = DatabasePodcast(
    id = id,
    name = name,
    description = description,
    images = serializer.serialize(images),
    links = serializer.serialize(links)
)

internal fun DatabasePodcast.toPodcast(serializer: Serializer): Podcast = Podcast(
    id = id,
    name = name,
    description = description,
    images = serializer.deserialize(images),
    links = serializer.deserialize(links)
)


internal fun RemotePodcastEpisode.toDatabasePodcastEpisode(serializer: Serializer): DatabasePodcastEpisode = DatabasePodcastEpisode(
    id = id,
    name = name,
    description = description,
    images = serializer.serialize(images),
    links = serializer.serialize(links),
    duration = duration,
    releaseDate = releaseDate,
    podcastId = podcastId,
    showId = showId?.let { serializer.serialize(it) },
    artistId = artistId?.let { serializer.serialize(it) }
)

internal fun DatabasePodcastEpisode.toPodcastEpisode(serializer: Serializer): PodcastEpisode = PodcastEpisode(
    id = id,
    name = name,
    description = description,
    images = serializer.deserialize(images),
    links = serializer.deserialize(links),
    duration = duration,
    releaseDate = releaseDate,
    podcastId = podcastId,
    showId = showId?.let { serializer.deserialize(it) },
    artistId = artistId?.let { serializer.deserialize(it) }
)