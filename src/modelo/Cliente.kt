package modelo

class Cliente(
    nombre: String,
    contacto: String,
    val direccion: String,
    val correo: String,
    val zona: String
) : Usuario(nombre, contacto) {
    // Contrato de igualdad: identidad logica por nombre y correo, sin importar mayusculas.
    private fun clave() = nombre.trim().lowercase() to correo.trim().lowercase()

    override fun equals(other: Any?): Boolean =
        this === other || (other is Cliente && clave() == other.clave())

    override fun hashCode(): Int = clave().hashCode()

    // Desestructuracion solicitada: (nombre, correo, telefono) = cliente
    operator fun component1(): String = nombre
    operator fun component2(): String = correo
    operator fun component3(): String = contacto

    override fun mostrarInformacion(): String =
        "Nombre: $nombre | Telefono: $contacto | Direccion: $direccion | Correo: $correo | Zona: $zona"
}
