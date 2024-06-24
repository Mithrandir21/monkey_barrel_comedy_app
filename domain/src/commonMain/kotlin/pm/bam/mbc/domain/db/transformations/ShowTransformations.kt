package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.datetime.DateTimeParsing
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.toCategory
import pm.bam.mbc.domain.models.toShowSchedule
import pm.bam.mbc.remote.models.RemoteShow
import pm.bam.mbc.remote.models.mapIds
import pmbammbcdomain.DatabaseShow

internal fun RemoteShow.toShow(dateTimeParsing: DateTimeParsing): Show = Show(
    id = id,
    name = title,
    description = description,
    url = url,
    images = images,
    categories = categories?.map { it.toCategory() },
    artistIds = artistIds.mapIds(),
    merchIds = merchIds.mapIds(),
    schedule = schedule.map { it.toShowSchedule(dateTimeParsing) }
)

internal fun Show.toDatabaseShow(serializer: Serializer): DatabaseShow = DatabaseShow(
    id = id,
    title = name,
    description = description,
    url = url,
    images = serializer.serialize(images),
    category = categories?.let { serializer.serialize(it) },
    artistIds = artistIds?.let { serializer.serialize(it) },
    merchIds = merchIds?.let { serializer.serialize(it) },
    schedule = schedule.let { serializer.serialize(it) }
)

internal fun DatabaseShow.toShow(serializer: Serializer): Show = Show(
    id = id,
    name = title,
    description = description,
    url = url,
    images = serializer.deserialize(images),
    categories = category?.let { serializer.deserialize(it) },
    artistIds = artistIds?.let { serializer.deserialize(it) },
    merchIds = merchIds?.let { serializer.deserialize(it) },
    schedule = serializer.deserialize(schedule)
)