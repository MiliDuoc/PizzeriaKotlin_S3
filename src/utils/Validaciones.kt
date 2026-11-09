package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

private val PATRON_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
private val PATRON_TELEFONO = Regex("^[0-9]{12}$")
private val FORMATO_FECHA = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT)

fun validarCorreo(dato: String): Boolean = PATRON_CORREO.matches(dato.trim())

// 12 digitos para el patron solicitado +XX (XXX) XXX-XXXX.
fun formatearTelefono(entrada: String): String? {
    val digitos = entrada.filter { it.isDigit() }
    if (!PATRON_TELEFONO.matches(digitos)) return null
    return "+${digitos.substring(0, 2)} (${digitos.substring(2, 5)}) " +
        "${digitos.substring(5, 8)}-${digitos.substring(8, 12)}"
}

fun validarFecha(texto: String): LocalDate? = try {
    LocalDate.parse(texto.trim(), FORMATO_FECHA)
} catch (_: DateTimeParseException) { null }

fun leerTexto(etiqueta: String): String {
    while (true) {
        print(etiqueta)
        val s = readlnOrNull()?.trim() ?: error("Entrada finalizada")
        if (s.isNotBlank()) return s
        println("Dato requerido; intente otra vez.")
    }
}

fun leerCorreo(): String {
    while (true) {
        val s = leerTexto("Correo: ")
        if (validarCorreo(s)) return s
        println("Correo invalido: use nombre@dominio.com")
    }
}

fun leerTelefono(): String {
    while (true) {
        val s = leerTexto("Telefono (12 digitos, ej. 569123456789): ")
        formatearTelefono(s)?.let { return it }
        println("Telefono invalido; se requiere +XX (XXX) XXX-XXXX (12 digitos).")
    }
}

fun leerFecha(): LocalDate {
    while (true) {
        val s = leerTexto("Fecha pedido (AAAA-MM-DD): ")
        validarFecha(s)?.let { return it }
        println("Fecha invalida: revise formato o dia del mes.")
    }
}

fun leerNumero(etiqueta: String, rango: IntRange): Int {
    while (true) {
        val s = leerTexto(etiqueta)
        val n = s.toIntOrNull()
        if (n != null && n in rango) return n
        println("Numero invalido; rango permitido ${rango.first}..${rango.last}.")
    }
}
