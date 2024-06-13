package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.EventStatus
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.remote.models.RemoteEventStatus
import pm.bam.mbc.remote.models.RemoteShow
import pmbammbcdomain.DatabaseShow

internal fun RemoteShow.toDatabaseShow(serializer: Serializer): DatabaseShow = DatabaseShow(
    id = id,
    title = title,
    url = url,
    venue = venues.first().name,
    images = serializer.serialize(images),
    eventStatus = serializer.serialize(eventStatus.toEventStatus()),
    description = description,
    category = categories?.let { serializer.serialize(it) },
    artistIds = artistIds?.let { serializer.serialize(it) },
    startDate = startDate,
    endDate = endDate,
)

internal fun DatabaseShow.toShow(serializer: Serializer): Show = Show(
    id = id,
    name = title,
    description = description,
    url = url,
    venue = venue,
    images = serializer.deserialize(images),
    eventStatus = serializer.deserialize(eventStatus),
    category = category?.let { serializer.deserialize(it) },
    artistIds = artistIds?.let { serializer.deserialize(it) },
    startDate = startDate,
    endDate = endDate,
)

internal fun RemoteEventStatus.toEventStatus(): EventStatus = when (this) {
    RemoteEventStatus.ACTIVE -> EventStatus.ACTIVE
    RemoteEventStatus.CANCELLED -> EventStatus.CANCELLED
}