import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object FernandoModule {

    fun actualizarDocumento(id: String, revActual: String, nuevoNombre: String, nuevaEdad: Int): String {
        println("\n[Fernando] Actualizando documento bajo control de versiones MVCC...")
        val updateJson = JSONObject().apply {
            put("_rev", revActual) // Obligatorio para validar concurrencia
            put("nombre", nuevoNombre)
            put("carrera", "Ingenieria de Sistemas")
            put("edad", nuevaEdad)
        }
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME/$id")
            .put(updateJson.toString().toRequestBody(JSON_MEDIA))
            .header("Authorization", AUTH_CREDENTIALS)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (response.code == 409) {
                println("[Fernando - UPDATE] Conflicto 409 detectado: La revision proporcionada es invalida.")
                return ""
            }
            val responseBody = JSONObject(response.body?.string() ?: "{}")
            val nuevaRevision = responseBody.optString("rev")
            println("[Fernando - UPDATE] Documento actualizado correctamente.")
            println(" -> Codigo HTTP: ${response.code}")
            println(" -> Nueva revision generada: $nuevaRevision")
            return nuevaRevision
        }
    }

    fun eliminarDocumento(id: String, revParaEliminar: String) {
        println("\n[Fernando] Eliminando documento proporcionando token de revision...")
        val request = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME/$id?rev=$revParaEliminar")
            .delete()
            .header("Authorization", AUTH_CREDENTIALS)
            .build()

        httpClient.newCall(request).execute().use { response ->
            println("[Fernando - DELETE] Documento eliminado satisfactoriamente.")
            println(" -> Codigo HTTP: ${response.code}")
            println(" -> Respuesta de CouchDB: ${response.body?.string()}")
        }
    }
}
