import modelo.*
import servicios.*
import utils.*
import java.time.LocalDate

// Ejecutar en IntelliJ seleccionando Run 'PruebasKt' si se desea revisar casos.
fun main() {
    check(!validarCorreo("correo-malo"))
    check(validarCorreo("uno@ejemplo.cl"))
    check(formatearTelefono("569123456789") == "+56 (912) 345-6789")
    check(validarFecha("2026-02-30") == null)
    check(enPeriodoPromocional(LocalDate.of(2026, 11, 9)))
    check(!enPeriodoPromocional(LocalDate.of(2026, 12, 1)))
    val c1 = Cliente("Maria", "123", "A", "maria@correo.cl", "Centro")
    val c2 = Cliente("MARIA", "456", "B", "MARIA@CORREO.CL", "Sur")
    check(c1 == c2 && c1.hashCode() == c2.hashCode())
    val p1 = PizzaPromocionable("Pepperoni", 12000.0, mapOf("Masa" to 1))
    val p2 = Producto("PEPPERONI", 9000.0, emptyMap(), "PIZZA")
    check(p1 == p2 && p1.hashCode() == p2.hashCode())
    check(p1.javaClass.getAnnotation(Promocionable::class.java) != null)
    val fecha = LocalDate.of(2026,11,9)
    val pedido1 = Pedido(1,c1,listOf(LineaPedido(p1,2)),fecha)
    val pedido2 = Pedido(2,c2,listOf(LineaPedido(p1,2)),fecha)
    val unido = pedido1 + pedido2
    check(unido.productos.single().cantidad == 4)
    check(unido.calcularSubtotal() == 48000.0)
    check(unido.calcularDescuento() == 7200.0) // 10% por 4 pizzas + 5% promocion
    check(unido.calcularTotal() == 48552.0)
    val (_, lista, total) = unido
    check(lista.size == 1 && total == 48552.0)
    val stock = mutableMapOf("Masa" to 2)
    check(!verificarYActualizarInventario(stock,listOf(LineaPedido(p1,3))))
    check(stock["Masa"] == 2)
    val servicio = ServicioPedidos()
    val ped = servicio.generarPedido(c1,listOf(LineaPedido(servicio.menu[0],1)),fecha)!!
    check(ped.estado == "En preparacion")
    servicio.prepararPedido(ped)
    check(servicio.despacharPedido(ped))
    check(ped.repartidor?.nombre == "Laura")
    servicio.entregarPedido(ped)
    check(ped.estado == "Entregado")
    println("PRUEBAS OK: validaciones, anotaciones, igualdad, combinacion, descuentos, stock y estados")
}
