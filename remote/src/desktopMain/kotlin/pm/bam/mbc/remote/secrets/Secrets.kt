package pm.bam.mbc.remote.secrets

actual fun getSecrets(): Secrets = object : Secrets {
    override val supaBaseUrl: String = System.getenv(Secrets.SUPABASE_URL)
    override val supaBaseKey: String = System.getenv(Secrets.SUPABASE_KEY)
}