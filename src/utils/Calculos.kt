package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

const val TASA_IVA = 0.19
const val TASA_DESCUENTO = 0.10
const val TASA_PROMOCIONAL = 0.05

fun calcularSubtotal(precioUnitario: Double, cantidad: Int): Double {
    require(precioUnitario.isFinite() && precioUnitario >= 0 && cantidad in 1..100)
    return precioUnitario * cantidad
}

fun calcularDescuentoCantidad(subtotal: Double, cantidadPizzas: Int): Double =
    if (cantidadPizzas > 3) subtotal * TASA_DESCUENTO else 0.0

fun calcularImpuesto(neto: Double): Double = neto * TASA_IVA

fun calcularTotal(subtotal: Double, descuentos: Double): Double {
    require(subtotal >= 0 && descuentos in 0.0..subtotal) { "Montos invalidos." }
    val neto = subtotal - descuentos
    return neto + calcularImpuesto(neto)
}

fun enPeriodoPromocional(fecha: LocalDate): Boolean =
    fecha in LocalDate.of(2026, 11, 1)..LocalDate.of(2026, 11, 30)

fun formatoPesos(valor: Double): String = "$" + "%,.0f".format(Locale.US, valor)
