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
    name = name,
    url = url,
    venue = venue,
    images = serializer.serialize(images),
    eventStatus = serializer.serialize(eventStatus.toEventStatus()),
    description = description,
    category = category?.let { serializer.serialize(it) },
    artistIds = artistIds?.let { serializer.serialize(it) },
    startDate = startDate,
    endDate = endDate,
)

internal fun DatabaseShow.toShow(serializer: Serializer): Show = Show(
    id = id,
    name = name,
    url = url,
    venue = venue,
    images = serializer.deserialize(images),
    eventStatus = serializer.deserialize(eventStatus),
    description = description,
    category = category?.let { serializer.deserialize(it) },
    artistIds = artistIds?.let { serializer.deserialize(it) },
    startDate = startDate,
    endDate = endDate,
)

internal fun RemoteEventStatus.toEventStatus(): EventStatus = when (this) {
    RemoteEventStatus.ACTIVE -> EventStatus.ACTIVE
    RemoteEventStatus.CANCELLED -> EventStatus.CANCELLED
}