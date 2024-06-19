package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Merch(
    val id: Long,
    val name: String,
    val description: String
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