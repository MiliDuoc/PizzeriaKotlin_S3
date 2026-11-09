package modelo

open class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>,
    val categoria: String = "pizza"
) {
    init {
        require(nombre.isNotBlank()) { "El nombre del producto no puede estar vacio." }
        require(precio.isFinite() && precio >= 0) { "El precio debe ser valido y no negativo." }
        require(ingredientes.values.all { it > 0 }) { "Los ingredientes deben tener cantidades positivas." }
    }

    private fun clave(): Pair<String, String> =
        nombre.trim().lowercase() to categoria.trim().lowercase()

    // Kotlin utiliza equals() para el operador ==; no se sobrecarga == directamente.
    override fun equals(other: Any?): Boolean =
        this === other || (other is Producto && clave() == other.clave())

    override fun hashCode(): Int = clave().hashCode()

    fun esPromocionable(): Boolean =
        javaClass.isAnnotationPresent(Promocionable::class.java)

    fun detallarProducto(): String =
        "$nombre [$categoria], neto $precio, ingredientes: " +
        ingredientes.entries.joinToString { (ingrediente, cantidad) -> "$ingrediente x$cantidad" }
}

@Promocionable("Pizza seleccionada para promociones de temporada")
class PizzaPromocionable(
    nombre: String, precio: Double, ingredientes: Map<String, Int>
) : Producto(nombre, precio, ingredientes, "pizza")

@Promocionable("Combo participante de promociones especiales")
class ComboPromocionable(
    nombre: String, precio: Double, ingredientes: Map<String, Int>
) : Producto(nombre, precio, ingredientes, "combo")
