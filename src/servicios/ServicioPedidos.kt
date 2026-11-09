package servicios

import modelo.*
import utils.*
import java.time.LocalDate

class ServicioPedidos {
    val menu: List<Producto> = listOf(
        PizzaPromocionable("Pepperoni", 12000.0, mapOf("Masa" to 1, "Queso" to 2, "Pepperoni" to 3)),
        PizzaPromocionable("Cuatro Quesos", 14000.0, mapOf("Masa" to 1, "Queso" to 4)),
        Producto("Vegetariana", 11000.0, mapOf("Masa" to 1, "Queso" to 1, "Verduras" to 3)),
        ComboPromocionable("Combo Familiar", 22000.0, mapOf("Masa" to 2, "Queso" to 3, "Pepperoni" to 2))
    )
    val clientes = mutableSetOf<Cliente>()
    val inventario = mutableMapOf("Masa" to 50, "Queso" to 90, "Pepperoni" to 70, "Verduras" to 40)
    val pedidos = mutableListOf<Pedido>()
    val repartidores = mutableListOf(
        Repartidor("Pedro", "569111111111", "Centro", 5.0),
        Repartidor("Laura", "569222222222", "Centro", 2.5),
        Repartidor("Carlos", "569333333333", "Norte", 3.0)
    )
    private var proximoId = 1

    fun registrarCliente(cliente: Cliente): Cliente {
        val existente = clientes.find { it == cliente }
        if (existente != null) {
            println("Cliente ya registrado; se reutiliza su ficha.")
            return existente
        }
        clientes.add(cliente)
        return cliente
    }

    fun registrarProductosSinDuplicados(productos: List<Producto>): Set<Producto> = productos.toSet()

    fun generarPedido(cliente: Cliente, lineas: List<LineaPedido>, fecha: LocalDate): Pedido? {
        if (!verificarYActualizarInventario(inventario, lineas)) return null
        val pedido = Pedido(proximoId++, registrarCliente(cliente), lineas, fecha)
        pedidos.add(pedido)
        println("Pedido #${pedido.id} creado; estado: ${pedido.estado}")
        return pedido
    }

    fun prepararPedido(pedido: Pedido) = pedido.cambiarEstado("Listo")

    fun despacharPedido(pedido: Pedido): Boolean {
        if (pedido.estado != "Listo") {
            println("El pedido debe estar Listo antes de enviarse.")
            return false
        }
        val elegido = repartidores.filter { it.disponible && it.zona == pedido.cliente.zona }
            .minByOrNull { it.distanciaKm }
        if (elegido == null || !elegido.tomarPedido()) {
            println("No hay repartidor disponible para la zona.")
            return false
        }
        pedido.repartidor = elegido
        pedido.cambiarEstado("En camino")
        println("Repartidor mas cercano en ${elegido.zona}: ${elegido.nombre}, ${elegido.distanciaKm} km")
        return true
    }

    fun entregarPedido(pedido: Pedido) {
        require(pedido.estado == "En camino") { "El pedido aun no esta en camino." }
        println(pedido.repartidor?.entregarPedido() ?: "Sin repartidor asignado")
        pedido.cambiarEstado("Entregado")
    }

    fun reporte() {
        println("===== REPORTE DEL DIA =====")
        pedidos.forEach { println("#${it.id}: ${it.cliente.nombre} | ${formatoPesos(it.calcularTotal())} | ${it.estado}") }
        println("Total pedidos: ${pedidos.size} | Entregados: ${pedidos.count { it.estado == "Entregado" }}")
    }
}
