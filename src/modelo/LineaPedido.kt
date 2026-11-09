package modelo

data class LineaPedido(val producto: Producto, val cantidad: Int) {
    init { require(cantidad in 1..100) { "Cantidad permitida: 1 a 100." } }
    fun subtotal(): Double = producto.precio * cantidad
}
