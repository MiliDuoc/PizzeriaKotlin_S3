import modelo.Cliente
import modelo.LineaPedido
import servicios.ServicioPedidos
import servicios.demostracionSumativa
import utils.*

fun main() {
    val servicio = ServicioPedidos()
    demostracionSumativa(servicio)
    println("===== PIZZERIA - SUMATIVA 1 (SEMANA 3) =====")
    while (true) {
        println("\n1. Nuevo pedido | 2. Preparar/Despachar | 3. Entregar | 4. Reporte | 0. Salir")
        val opcion = try { leerNumero("Seleccione: ", 0..4) }
        catch (_: IllegalStateException) { break }
        when (opcion) {
            0 -> { servicio.reporte(); return }
            1 -> {
                try {
                    val nombre = leerTexto("Nombre: ")
                    val telefono = leerTelefono()
                    val direccion = leerTexto("Direccion: ")
                    val correo = leerCorreo()
                    val zona = when (leerNumero("Zona: 1 Centro, 2 Norte, 3 Sur: ", 1..3)) {
                        1 -> "Centro"; 2 -> "Norte"; else -> "Sur"
                    }
                    val cliente = Cliente(nombre, telefono, direccion, correo, zona)
                    val fecha = leerFecha()
                    servicio.menu.forEachIndexed { i, p -> println("${i+1}. ${p.detallarProducto()}") }
                    val indice = leerNumero("Producto (1-${servicio.menu.size}): ", 1..servicio.menu.size)
                    val cantidad = leerNumero("Cantidad (1-100): ", 1..100)
                    val pedido = servicio.generarPedido(cliente, listOf(LineaPedido(servicio.menu[indice - 1], cantidad)), fecha)
                    if (pedido != null) println(pedido.resumen()) else println("Pedido rechazado por inventario.")
                } catch (e: IllegalArgumentException) { println("Error en datos: ${e.message}") }
            }
            2 -> {
                val id = leerNumero("ID de pedido: ", 1..100000)
                val pedido = servicio.pedidos.find { it.id == id }
                if (pedido == null) println("Pedido no encontrado")
                else try {
                    if (pedido.estado == "En preparacion") servicio.prepararPedido(pedido)
                    if (pedido.estado == "Listo") servicio.despacharPedido(pedido)
                    println(pedido.resumen())
                } catch (e: IllegalArgumentException) { println("Error: ${e.message}") }
            }
            3 -> {
                val id = leerNumero("ID de pedido: ", 1..100000)
                val pedido = servicio.pedidos.find { it.id == id }
                if (pedido == null) println("Pedido no encontrado")
                else try { servicio.entregarPedido(pedido); println(pedido.resumen()) }
                catch (e: IllegalArgumentException) { println("Error: ${e.message}") }
            }
            4 -> servicio.reporte()
        }
    }
}
