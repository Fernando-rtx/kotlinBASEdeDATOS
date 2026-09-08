import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object MarioModule {
    fun crearDocumento(id: String, nombre: String, carrera: String, edad: Int): String {
        println("\n[Mario] Creando nuevo documento con ID: '$id'...")
        val docJson = JSONObject().apply {
            put("nombre", nombre)
            put("carrera", carrera)
            put("edad", edad)
        }
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME/$id")
            .put(docJson.toString().toRequestBody(JSON_MEDIA))
            .header("Authorization", AUTH_CREDENTIALS)
            .build()
        httpClient.newCall(request).execute().use { response ->
            val responseBody = JSONObject(response.body?.string() ?: "{}")
            val revisionGenerada = responseBody.optString("rev")
            println("[Mario - CREATE] Documento registrado exitosamente.")
            println(" -> Codigo HTTP: ${response.code}")
            println(" -> Revision inicial asignada: $revisionGenerada")
            return revisionGenerada
        }
    }
}
