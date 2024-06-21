package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.domain.models.MerchItem
import pm.bam.mbc.domain.models.toMerchItemStatus
import pm.bam.mbc.domain.models.toMerchItemType
import pm.bam.mbc.remote.models.RemoteMerch
import pm.bam.mbc.remote.models.RemoteMerchItem
import pmbammbcdomain.DatabaseMerch
import pmbammbcdomain.DatabaseMerchItem

internal fun RemoteMerch.toMerch(): Merch = Merch(
    id = id,
    name = name,
    description = description,
    images = images
)

internal fun Merch.toDatabaseMerch(serializer: Serializer): DatabaseMerch = DatabaseMerch(
    id = id,
    name = name,
    description = description,
    images = serializer.serialize(images)
)

internal fun DatabaseMerch.toMerch(serializer: Serializer): Merch = Merch(
    id = id,
    name = name,
    description = description,
    images = serializer.deserialize(images)
)


internal fun RemoteMerchItem.toMerchItem(): MerchItem = MerchItem(
    id = id,
    name = name,
    description = description,
    status = status.toMerchItemStatus(),
    itemTypes = itemTypes.map { it.toMerchItemType() },
    merchId = merchId
)

internal fun MerchItem.toDatabaseMerchItem(serializer: Serializer): DatabaseMerchItem = DatabaseMerchItem(
    id = id,
    name = name,
    description = description,
    status = serializer.serialize(status),
    itemTypes = serializer.serialize(itemTypes),
    merchId = merchId
)

internal fun DatabaseMerchItem.toMerchItem(serializer: Serializer): MerchItem = MerchItem(
    id = id,
    name = name,
    description = description,
    status = serializer.deserialize(status),
    itemTypes = serializer.deserialize(itemTypes),
    merchId = merchId
)