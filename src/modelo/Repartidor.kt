package modelo

class Repartidor(
    nombre: String,
    contacto: String,
    val zona: String,
    val distanciaKm: Double,
    var disponible: Boolean = true
) : Usuario(nombre, contacto) {
    override fun mostrarInformacion(): String =
        "Repartidor: $nombre | Zona: $zona | Distancia: $distanciaKm km | Disponible: $disponible"

    fun tomarPedido(): Boolean {
        if (!disponible) return false
        disponible = false
        return true
    }

    fun entregarPedido(): String {
        disponible = true
        return "Pedido entregado por $nombre."
    }
}
