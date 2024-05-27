package pm.bam.mbc.remote.logic

actual fun getRemoteBuildUtil(): RemoteBuildUtil = RemoteBuildUtilImpl()

internal class RemoteBuildUtilImpl : RemoteBuildUtil {

    /** Get an enum representing the [RemoteBuildType] of this class. */
    override fun buildType(): RemoteBuildType =
        //  TODO - Figure out Build type in KMM - when (val type = BuildConfig.BUILD_TYPE) {
        when (1) {
            1 -> RemoteBuildType.DEBUG
            2 -> RemoteBuildType.RELEASE
            else -> throw RuntimeException("Unexpected build type")
        }
}
