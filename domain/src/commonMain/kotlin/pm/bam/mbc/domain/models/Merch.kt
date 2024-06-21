package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable
import pm.bam.mbc.remote.models.RemoteMerchItemStatus
import pm.bam.mbc.remote.models.RemoteMerchItemType

@Serializable
data class Merch(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>
)


@Serializable
data class MerchItem(
    val id: Long,
    val name: String,
    val description: String? = null,
    val status: MerchItemStatus,
    val itemTypes: List<MerchItemType>,
    val merchId: Long
)

enum class MerchItemType {
    DIGITAL,
    CLOTHING,
    VINYL,
    SIGNED,
    LIMITED_EDITION
}

enum class MerchItemStatus {
    PRE_ORDER,
    IN_STOCK,
    OUT_OF_STOCK,
    DISCOTINUED
}

internal fun RemoteMerchItemType.toMerchItemType(): MerchItemType = when (this) {
    RemoteMerchItemType.DIGITAL -> MerchItemType.DIGITAL
    RemoteMerchItemType.CLOTHING -> MerchItemType.CLOTHING
    RemoteMerchItemType.VINYL -> MerchItemType.VINYL
    RemoteMerchItemType.SIGNED -> MerchItemType.SIGNED
    RemoteMerchItemType.LIMITED_EDITION -> MerchItemType.LIMITED_EDITION
}

internal fun RemoteMerchItemStatus.toMerchItemStatus(): MerchItemStatus = when (this) {
    RemoteMerchItemStatus.PRE_ORDER -> MerchItemStatus.PRE_ORDER
    RemoteMerchItemStatus.IN_STOCK -> MerchItemStatus.IN_STOCK
    RemoteMerchItemStatus.OUT_OF_STOCK -> MerchItemStatus.OUT_OF_STOCK
    RemoteMerchItemStatus.DISCOTINUED -> MerchItemStatus.DISCOTINUED
}