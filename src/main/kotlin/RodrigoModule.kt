import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object RodrigoModule {
    fun crearBaseDatos() {
        println("[Rodrigo] Verificando e inicializando base de datos '$DB_NAME'...")
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME")
            .put("".toRequestBody())
            .header("Authorization", AUTH_CREDENTIALS)
            .build()
        httpClient.newCall(request).execute().use { response ->
            when (response.code) {
                201 -> println("[Rodrigo] Base de datos creada con exito (HTTP 201).")
                412 -> println("[Rodrigo] La base de datos ya existia previamente (HTTP 412).")
                else -> println("[Rodrigo] Respuesta inesperada del servidor: HTTP ${response.code}")
            }
        }
    }
}
