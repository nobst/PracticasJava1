package Tienda;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PedidoJsonService {

    private final String archivo = "pedidos.json";

    public void guardarPedidoJson(Pedido pedido) {
        try (FileWriter writer = new FileWriter(archivo, true)) {
            writer.write("{");
            writer.write("  \"cliente\": \"" + pedido.getCliente().getNombre() + "\",");
            writer.write("  \"estado\": \"" + pedido.getEstado() + "\",");
            writer.write("  \"total\": " + pedido.calcularTotal() + "");
            writer.write("}");
            writer.write("-------------------");
            System.out.println("Pedido guardado en archivo: " + archivo);
        } catch (IOException e) {
            System.out.println("Error guardando JSON: " + e.getMessage());
        }
    }

    public void generarReporteVentas(List<Pedido> pedidos) {
        double totalVentas = 0;
        int confirmados = 0;

        for (Pedido p : pedidos) {
            if (p.getEstado() == EstadoPedido.CONFIRMADO) {
                totalVentas += p.calcularTotal();
                confirmados++;
            }
        }

        System.out.println("===== REPORTE DE VENTAS =====");
        System.out.println("Pedidos confirmados: " + confirmados);
        System.out.println("Total vendido: " + totalVentas);
        System.out.println("Archivo generado: " + archivo);
    }
}