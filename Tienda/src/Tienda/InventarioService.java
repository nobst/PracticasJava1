
package Tienda;
import java.util.*;
import java.util.stream.Collectors;

public class InventarioService {

    public void confirmarPedido(Pedido pedido) {
        // Validar stock primero
        for (ItemPedido item : pedido.getItems()) {
            if (item.getProducto().getStock() < item.getCantidad()) {
                throw new IllegalStateException(
                        "Pedido rechazado: stock insuficiente para "
                        + item.getProducto().getNombre());
            }
        }

        // Descontar después de validar todo
        for (ItemPedido item : pedido.getItems()) {
            item.getProducto().descontarStock(item.getCantidad());
        }

        pedido.setEstado(EstadoPedido.CONFIRMADO);
    }

    public List<Producto> productosConStockBajo(List<Producto> productos, int umbral) {
        return productos.stream()
                .filter(p -> p.getStock() < umbral)
                .toList();
    }

    public List<Producto> top5MasVendidos(List<Pedido> pedidos) {
        Map<Producto, Integer> ventas = new HashMap<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.CONFIRMADO) {
                for (ItemPedido item : pedido.getItems()) {
                    ventas.merge(item.getProducto(), item.getCantidad(), Integer::sum);
                }
            }
        }

        return ventas.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }
    
  
}