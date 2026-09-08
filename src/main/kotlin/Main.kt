import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
val httpClient = OkHttpClient()
const val COUCHDB_URL = "http://localhost:5984"
const val DB_NAME = "universidad"
// EXPO ESTANDAR (cada maquina): admin / password — coincide con docker run -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password
// Si usas el servidor de casa (~/servidores/couchdb usa fernandojose), cambia "password" por "fernandojose".
val AUTH_CREDENTIALS = Credentials.basic("admin", "password")

fun main() {
    // SOLO DEMO AISLADA FERNANDO
    val docId = "est_2026_01"

    val putDbRequest = Request.Builder()
        .url("$COUCHDB_URL/$DB_NAME")
        .put("{}".toRequestBody(JSON_MEDIA))
        .header("Authorization", AUTH_CREDENTIALS)
        .build()
    httpClient.newCall(putDbRequest).execute().use { resp ->
        println("PUT DB -> ${resp.code}")
    }

    var revActual: String? = null
    val getRequest = Request.Builder()
        .url("$COUCHDB_URL/$DB_NAME/$docId")
        .get()
        .header("Authorization", AUTH_CREDENTIALS)
        .build()
    httpClient.newCall(getRequest).execute().use { resp ->
        if (resp.code == 200) {
            val body = JSONObject(resp.body!!.string())
            revActual = body.getString("_rev")
            println("Doc $docId ya existe, rev=$revActual")
        }
    }

    if (revActual == null) {
        val temporal = JSONObject()
            .put("nombre", "Temporal Fernando")
            .put("carrera", "Ingenieria de Sistemas")
            .put("edad", 20)
            .toString()
        val putDocRequest = Request.Builder()
            .url("$COUCHDB_URL/$DB_NAME/$docId")
            .put(temporal.toRequestBody(JSON_MEDIA))
            .header("Authorization", AUTH_CREDENTIALS)
            .build()
        httpClient.newCall(putDocRequest).execute().use { resp ->
            val bodyStr = resp.body!!.string()
            println("PUT doc temporal -> ${resp.code} $bodyStr")
            if (resp.code == 201) {
                revActual = JSONObject(bodyStr).getString("rev")
            }
        }
    }

    if (revActual != null) {
        val nuevaRev = FernandoModule.actualizarDocumento(docId, revActual!!, "Fernando Actualizado", 22)
        println("actualizarDocumento -> nuevaRev=$nuevaRev")
        val revParaEliminar = if (nuevaRev.isNotEmpty()) nuevaRev else revActual!!
        FernandoModule.eliminarDocumento(docId, revParaEliminar)
    } else {
        println("No se pudo obtener rev para demo aislada")
    }
}
