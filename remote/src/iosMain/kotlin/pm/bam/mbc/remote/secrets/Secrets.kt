package pm.bam.mbc.remote.secrets

// TODO - replace with actual secrets for iOS
actual fun getSecrets(): Secrets = object: Secrets {
    override val supaBaseUrl: String = "https://supabase.io"
    override val supaBaseKey: String = "supabaseKey"
}