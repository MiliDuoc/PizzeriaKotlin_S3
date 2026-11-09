package modelo

import java.time.LocalDate
import utils.calcularDescuentoCantidad
import utils.calcularImpuesto
import utils.calcularTotal
import utils.formatoPesos

class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<LineaPedido>,
    val fecha: LocalDate,
    var estado: String = "En preparacion",
    val notas: String? = null,
    var repartidor: Repartidor? = null
) {
    init { require(productos.isNotEmpty()) { "Un pedido debe incluir productos." } }

    val cantidadPizzas: Int get() = productos.filter { it.producto.categoria == "pizza" }.sumOf { it.cantidad }

    fun calcularSubtotal(): Double = productos.sumOf { it.subtotal() }

    // 10% si hay mas de tres pizzas; 5% adicional sobre productos promocionables
    // durante noviembre 2026. Ambos descuentos se calculan sobre el subtotal original.
    fun calcularDescuento(): Double {
        val descuentoCantidad = calcularDescuentoCantidad(calcularSubtotal(), cantidadPizzas)
        val descuentoPromocion = if (utils.enPeriodoPromocional(fecha))
            productos.filter { it.producto.esPromocionable() }.sumOf { it.subtotal() } * 0.05
        else 0.0
        return descuentoCantidad + descuentoPromocion
    }

    fun calcularImpuesto(): Double = calcularImpuesto(calcularSubtotal() - calcularDescuento())
    fun calcularTotal(): Double = calcularTotal(calcularSubtotal(), calcularDescuento())

    // Combinar pedidos del mismo cliente, sumar cantidades del mismo producto y recalcular.
    operator fun plus(otro: Pedido): Pedido {
        require(cliente == otro.cliente) { "Solo se combinan pedidos del mismo cliente." }
        val agrupadas = (productos + otro.productos).groupBy { it.producto }
            .map { (producto, lineas) -> LineaPedido(producto, lineas.sumOf { it.cantidad }) }
        return Pedido(
            id = minOf(id, otro.id), cliente = cliente, productos = agrupadas,
            fecha = maxOf(fecha, otro.fecha), notas = "Pedidos $id y ${otro.id} combinados"
        )
    }

    // Desestructuracion: (cliente, productos, total) = pedido
    operator fun component1(): Cliente = cliente
    operator fun component2(): List<LineaPedido> = productos
    operator fun component3(): Double = calcularTotal()

    fun cambiarEstado(nuevo: String) {
        val transiciones = mapOf(
            "En preparacion" to setOf("Listo"),
            "Listo" to setOf("En camino"),
            "En camino" to setOf("Entregado"),
            "Entregado" to emptySet()
        )
        require(nuevo in (transiciones[estado] ?: emptySet())) {
            "Transicion invalida: $estado -> $nuevo"
        }
        estado = nuevo
    }

    fun resumen(): String = buildString {
        appendLine("==========================================")
        appendLine("PIZZERIA - RESUMEN | Pedido #$id | $fecha")
        appendLine("Cliente: ${cliente.mostrarInformacion()}")
        appendLine("--- PRODUCTOS ---")
        productos.forEach { (producto, cantidad) ->
            appendLine("- ${producto.nombre} ($cantidad) = ${formatoPesos(producto.precio * cantidad)}")
        }
        appendLine("Subtotal: ${formatoPesos(calcularSubtotal())}")
        appendLine("Descuentos: ${formatoPesos(calcularDescuento())}")
        appendLine("IVA 19%: ${formatoPesos(calcularImpuesto())}")
        appendLine("TOTAL: ${formatoPesos(calcularTotal())}")
        appendLine("Estado: $estado | Repartidor: ${repartidor?.nombre ?: "Sin asignar"}")
        appendLine("Notas: ${notas?.takeIf { it.isNotBlank() } ?: "Sin notas"}")
        appendLine("==========================================")
    }
}
