package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.remote.models.RemoteShow
import pmbammbcdomain.DatabaseShow

internal fun RemoteShow.toDatabaseShow(serializer: Serializer): DatabaseShow = DatabaseShow(
    id = id,
    title = title,
    url = url,
    images = serializer.serialize(images),
    description = description,
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
    category = category?.let { serializer.deserialize(it) },
    artistIds = artistIds?.let { serializer.deserialize(it) },
    merchIds = merchIds?.let { serializer.deserialize(it) },
    schedule = serializer.deserialize(schedule)
)