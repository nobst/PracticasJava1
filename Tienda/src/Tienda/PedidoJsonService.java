package Tienda;

import org.json.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class PedidoJsonService {

    private final String archivo = "pedidos.json";

    // ── Devuelve el siguiente ID disponible leyendo el JSON ───────────────────
    public long siguientePedidoId() {
        JSONArray array = cargarArray();
        long maxId = 0;
        for (int i = 0; i < array.length(); i++) {
            long id = array.getJSONObject(i).getLong("pedidoId");
            if (id > maxId) maxId = id;
        }
        return maxId + 1;
    }

    // ── Guarda un pedido nuevo acumulando en el JSON ──────────────────────────
    public void guardarPedidoJson(Pedido pedido) {
        JSONArray array = cargarArray();

        JSONArray itemsArray = new JSONArray();
        for (ItemPedido item : pedido.getItems()) {
            JSONObject itemObj = new JSONObject();
            itemObj.put("productoId",     item.getProducto().getId());
            itemObj.put("nombre",         item.getProducto().getNombre());
            itemObj.put("cantidad",       item.getCantidad());
            itemObj.put("precioUnitario", item.getPrecioUnitario());
            itemObj.put("subtotal",       item.getSubtotal());
            itemsArray.put(itemObj);
        }

        JSONObject obj = new JSONObject();
        obj.put("pedidoId",  pedido.getId());
        obj.put("clienteId", pedido.getCliente().getId());
        obj.put("cliente",   pedido.getCliente().getNombre());
        obj.put("fecha",     pedido.getFecha().toString());
        obj.put("estado",    pedido.getEstado().toString());
        obj.put("total",     pedido.calcularTotal());
        obj.put("items",     itemsArray);

        array.put(obj);
        escribirArchivo(array);
        System.out.println("Pedido #" + pedido.getId() + " guardado en: " + archivo);
    }

    // ── Historial completo de un cliente ──────────────────────────────────────
    public void mostrarHistorialCliente(int clienteId, String nombreCliente) {
        JSONArray array = cargarArray();

        List<JSONObject> pedidosCliente = new ArrayList<>();
        double totalGastado = 0;

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (obj.getInt("clienteId") == clienteId) {
                pedidosCliente.add(obj);
                totalGastado += obj.getDouble("total");
            }
        }

        if (pedidosCliente.isEmpty()) {
            System.out.println(nombreCliente + " no tiene pedidos registrados.");
            return;
        }

        System.out.println("====== HISTORIAL DE " + nombreCliente.toUpperCase() + " ======");
        System.out.println("Total de pedidos: " + pedidosCliente.size());
        System.out.printf("Total gastado:    $%.2f%n", totalGastado);
        System.out.println("----------------------------------------");

        for (JSONObject pedido : pedidosCliente) {
            System.out.println("Pedido #" + pedido.getLong("pedidoId")
                    + " | Fecha: "  + pedido.getString("fecha")
                    + " | Estado: " + pedido.getString("estado")
                    + " | Total: $" + pedido.getDouble("total"));

            JSONArray items = pedido.getJSONArray("items");
            for (int j = 0; j < items.length(); j++) {
                JSONObject item = items.getJSONObject(j);
                System.out.printf("   - %-25s x%d  @ $%.2f  = $%.2f%n",
                        item.getString("nombre"),
                        item.getInt("cantidad"),
                        item.getDouble("precioUnitario"),
                        item.getDouble("subtotal"));
            }
            System.out.println("----------------------------------------");
        }
    }

    // ── Reporte general de ventas ─────────────────────────────────────────────
    public void generarReporteVentas() {
        JSONArray array = cargarArray();
        double totalVentas = 0;
        int confirmados = 0;

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if ("CONFIRMADO".equals(obj.getString("estado"))) {
                totalVentas += obj.getDouble("total");
                confirmados++;
            }
        }

        System.out.println("===== REPORTE GENERAL DE VENTAS =====");
        System.out.println("Pedidos confirmados: " + confirmados);
        System.out.printf("Total vendido:       $%.2f%n", totalVentas);
    }

    // ── Carga el array JSON, si el archivo está corrupto lo reinicia ──────────
    private JSONArray cargarArray() {
        File file = new File(archivo);
        if (!file.exists()) return new JSONArray();

        try {
            String contenido = new String(Files.readAllBytes(file.toPath())).trim();
            if (contenido.isEmpty()) return new JSONArray();
            return new JSONArray(contenido);
        } catch (JSONException e) {
            // Archivo con formato viejo o corrupto: se reinicia limpio
            System.out.println("Archivo pedidos.json inválido, se reiniciará.");
            escribirArchivo(new JSONArray());
            return new JSONArray();
        } catch (IOException e) {
            System.out.println("Error leyendo pedidos: " + e.getMessage());
            return new JSONArray();
        }
    }

    private void escribirArchivo(JSONArray array) {
        try (FileWriter writer = new FileWriter(archivo, false)) {
            writer.write(array.toString(2));
        } catch (IOException e) {
            System.out.println("Error guardando pedidos: " + e.getMessage());
        }
    }
}