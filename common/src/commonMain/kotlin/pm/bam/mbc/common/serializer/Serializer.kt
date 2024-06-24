package pm.bam.mbc.common.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer

/**
 * Serializer class that facilitates Serialization/Deserialization.
 *
 * Note some additional extension functions allowing smoother functionality with reified, inline functionality.
 */
interface Serializer {

    /**
     * Serialize the given object [T], using the given [serializationStrategy], into a returned [String].
     *
     * @param T The object that is to be serialized.
     * @param serializationStrategy The [SerializationStrategy] for [T]. (Needed because of Java Erasure of generic T.)
     */
    fun <T : Any> serialize(deserialized: T, serializationStrategy: SerializationStrategy<T>): String

    /**
     * Deserialize the given [serialized] using the given [deserializationStrategy].
     *
     * @param serialized The string that is to be deserialized.
     * @param deserializationStrategy The [DeserializationStrategy] for [T].
     */
    fun <T : Any> deserialize(serialized: String, deserializationStrategy: DeserializationStrategy<T>): T
}


/**
 * Serialize the given object [T] into a returned [String].
 *
 * @param T The object that is to be serialized.
 */
inline fun <reified T : Any> Serializer.serialize(deserialized: T): String = serialize(deserialized, serializer())


/**
 * Deserialize the given [serialized] into an object of type [T].
 *
 * @param serialized The string that is to be deserialized.
 * @param T The type of object that is expected returned.
 */
inline fun <reified T : Any> Serializer.deserialize(serialized: String): T = deserialize(serialized, serializer())