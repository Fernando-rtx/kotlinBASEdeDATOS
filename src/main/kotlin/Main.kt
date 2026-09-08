import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient

val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
val httpClient = OkHttpClient()
const val COUCHDB_URL = "http://localhost:5984"
const val DB_NAME = "universidad"

// EXPO ESTANDAR (todas las maquinas): admin / password.
// Coincide con: docker run -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password
// Casa Fernando (~/servidores/couchdb usa fernandojose): cambia "password" por "fernandojose".
val AUTH_CREDENTIALS = Credentials.basic("admin", "password")

fun main() {
    println("====================================================")
    println(" DEMOSTRACION: KOTLIN + APACHE COUCHDB + DOCKER ")
    println("====================================================\n")
    // 1. FASE DE INFRAESTRUCTURA (Rodrigo)
    RodrigoModule.crearBaseDatos()
    val documentoId = "est_2026_01"
    // 2. FASE DE CREACION (Mario)
    val revisionV1 = MarioModule.crearDocumento(
        id = documentoId,
        nombre = "Juan Perez",
        carrera = "Ingenieria de Sistemas",
        edad = 22
    )
    // Si el doc ya existia (seed importada), Mario muestra el 409 y seguimos con la rev vigente.
    val revParaLeer = revisionV1.ifEmpty {
        AlbertoModule.obtenerRevision(documentoId)
    }
    // 3. FASE DE LECTURA Y CONSULTAS (Alberto)
    AlbertoModule.leerDocumentoPorId(documentoId)
    AlbertoModule.consultarPorCarrera("Ingenieria de Sistemas")
    // 4. FASE DE MODIFICACION, MVCC Y BORRADO (Fernando)
    val revBase = revisionV1.ifEmpty { revParaLeer }
    val revisionV2 = FernandoModule.actualizarDocumento(
        id = documentoId,
        revActual = revBase,
        nuevoNombre = "Juan Perez Actualizado",
        nuevaEdad = 23
    )
    if (revisionV2.isNotEmpty()) {
        FernandoModule.eliminarDocumento(documentoId, revisionV2)
    }
    println("\n====================================================")
    println(" DEMOSTRACION FINALIZADA CON EXITO ")
    println("====================================================")
}
