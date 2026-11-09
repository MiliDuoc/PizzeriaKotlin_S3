package servicios

import modelo.*
import utils.*
import java.time.LocalDate

fun demostracionSumativa(servicio: ServicioPedidos) {
    println("\n======== DEMOSTRACION SUMATIVA SEMANA 3 ========")
    println("1) REGEX, RANGES Y FECHAS")
    println("Correo valido: ${validarCorreo("maria@correo.cl")}")
    println("Correo invalido: ${validarCorreo("sin-arroba")}")
    println("Telefono formateado: ${formatearTelefono("569123456789")}")
    println("Cantidad 4 en 1..100: ${4 in 1..100}")
    val fecha = LocalDate.of(2026, 11, 9)
    println("$fecha dentro de promocion: ${enPeriodoPromocional(fecha)}")

    println("\n2) ANOTACIONES Y REFLECTION")
    servicio.menu.forEach { p ->
        val anotacion = p.javaClass.getAnnotation(Promocionable::class.java)
        println("${p.nombre}: ${anotacion?.detalle ?: "No promocionable"}")
    }
    // Reflection sobre atributos y metodos publicos (interop Java, sin kotlin-reflect externo).
    for (clase in listOf(Pedido::class.java, Producto::class.java)) {
        println("Clase: ${clase.simpleName}")
        println("Atributos: ${clase.declaredFields.filterNot { it.isSynthetic }.map { it.name }.sorted()}")
        println("Metodos: ${clase.declaredMethods.filterNot { it.isSynthetic }.map { it.name }.distinct().sorted()}")
    }

    println("\n3) IGUALDAD Y EVITAR DUPLICADOS")
    val maria = Cliente("Maria", "+56 (912) 345-6789", "Calle 1", "maria@correo.cl", "Centro")
    val mariaCopia = Cliente("MARIA", "+56 (912) 345-6789", "Otra calle", "MARIA@correo.cl", "Norte")
    println("Clientes equivalentes por nombre y correo: ${maria == mariaCopia}")
    println("Clientes unicos registrados: ${listOf(maria, mariaCopia).toSet().size}")
    val pepperoni = servicio.menu[0]
    val pepperoniCopia = Producto("pepperoni", 12000.0, emptyMap(), "PIZZA")
    println("Productos equivalentes por nombre y categoria: ${pepperoni == pepperoniCopia}")
    println("Productos sin duplicados: ${servicio.registrarProductosSinDuplicados(listOf(pepperoni, pepperoniCopia)).size}")

    println("\n4) OPERADOR +, CANTIDADES Y DESCUENTOS")
    val pedido1 = Pedido(101, maria, listOf(LineaPedido(pepperoni, 2)), fecha)
    val pedido2 = Pedido(102, mariaCopia, listOf(LineaPedido(pepperoni, 2), LineaPedido(servicio.menu[2], 1)), fecha)
    val combinado = pedido1 + pedido2
    println("Pedidos 101 + 102 combinados, lineas: ${combinado.productos.size}, pizzas: ${combinado.cantidadPizzas}")

    println("\n5) DESESTRUCTURACION CLIENTE Y PEDIDO")
    val (nombre, correo, telefono) = maria
    println("Cliente desestructurado: $nombre | $correo | $telefono")
    val (cliente, productos, total) = combinado
    println("Pedido desestructurado: ${cliente.nombre}, ${productos.size} lineas, ${formatoPesos(total)}")

    println("\n6) RESUMEN INTEGRADO")
    println(combinado.resumen())
    println("======== FIN DEMOSTRACION ========\n")
}
