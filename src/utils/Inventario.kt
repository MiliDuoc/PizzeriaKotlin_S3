package utils

import modelo.LineaPedido

fun verificarYActualizarInventario(
    inventario: MutableMap<String, Int>, lineas: List<LineaPedido>
): Boolean {
    val requeridos = mutableMapOf<String, Int>()
    lineas.forEach { (producto, cantidad) ->
        producto.ingredientes.forEach { (ingrediente, unidades) ->
            requeridos[ingrediente] = (requeridos[ingrediente] ?: 0) + unidades * cantidad
        }
    }
    val faltantes = requeridos.filter { (ingrediente, cantidad) ->
        (inventario[ingrediente] ?: 0) < cantidad
    }
    return when {
        faltantes.isNotEmpty() -> {
            println("Ingredientes insuficientes:")
            faltantes.forEach { (ingrediente, cantidad) ->
                println("- $ingrediente: reponer ${cantidad - (inventario[ingrediente] ?: 0)}")
            }
            false
        }
        else -> {
            requeridos.forEach { (ingrediente, cantidad) ->
                inventario[ingrediente] = (inventario[ingrediente] ?: 0) - cantidad
            }
            true
        }
    }
}
