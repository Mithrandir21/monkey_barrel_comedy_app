package pm.bam.mbc.remote.logic

actual fun getRemoteBuildUtil(): RemoteBuildUtil = RemoteBuildUtilImpl()

internal class RemoteBuildUtilImpl : RemoteBuildUtil {

    /** Get an enum representing the [RemoteBuildType] of this class. */
    override fun buildType(): RemoteBuildType = RemoteBuildType.DEBUG
}
