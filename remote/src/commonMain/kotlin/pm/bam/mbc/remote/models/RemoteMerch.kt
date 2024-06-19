package pm.bam.mbc.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMerch(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>
)


@Serializable
data class RemoteMerchItem(
    val id: Long,
    val name: String,
    @SerialName("item_description")
    val description: String? = null,
    val status: RemoteMerchItemStatus,
    @SerialName("types")
    val itemTypes: List<RemoteMerchItemType>,
    @SerialName("merch_id")
    val merchId: Long
)

enum class RemoteMerchItemType {
    DIGITAL,
    CLOTHING,
    VINYL,
    SIGNED,
    LIMITED_EDITION
}

enum class RemoteMerchItemStatus {
    PRE_ORDER,
    IN_STOCK,
    OUT_OF_STOCK,
    DISCOTINUED
}