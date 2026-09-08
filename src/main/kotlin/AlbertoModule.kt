import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object AlbertoModule {
    fun leerDocumentoPorId(id: String) {
        println("\n[Alberto] Leyendo documento por clave primaria (GET)...")
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME/$id")
            .get()
            .header("Authorization", AUTH_CREDENTIALS)
            .build()
        httpClient.newCall(request).execute().use { response ->
            println("[Alberto - READ] Codigo HTTP: ${response.code}")
            println(" -> Payload recibido: ${response.body?.string()}")
        }
    }

    fun consultarPorCarrera(carreraFiltro: String) {
        println("\n[Alberto] Ejecutando consulta declarativa Mango (_find)...")
        val queryMango = JSONObject().apply {
            put("selector", JSONObject().apply {
                put("carrera", JSONObject().put("\$eq", carreraFiltro))
            })
            put("fields", JSONArray(listOf("_id", "nombre", "carrera", "edad")))
        }
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME/_find")
            .post(queryMango.toString().toRequestBody(JSON_MEDIA))
            .header("Authorization", AUTH_CREDENTIALS)
            .build()
        httpClient.newCall(request).execute().use { response ->
            println("[Alberto - MANGO QUERY] Codigo HTTP: ${response.code}")
            println(" -> Registros coincidentes: ${response.body?.string()}")
        }
    }

    fun obtenerRevision(id: String): String {
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME/$id")
            .get()
            .header("Authorization", AUTH_CREDENTIALS)
            .build()
        httpClient.newCall(request).execute().use { response ->
            if (response.code != 200) return ""
            return JSONObject(response.body?.string() ?: "{}").optString("_rev")
        }
    }
}
