package Tienda;

import org.json.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ProductoJsonService {

    private final String archivo = "productos.json";

    // ── Carga productos desde productos.json ──────────────────────────────────
    public List<Producto> cargarProductos(Categoria categoria, Proveedor proveedor) {
        File file = new File(archivo);

        try {
            String contenido = new String(Files.readAllBytes(file.toPath()));
            JSONArray array = new JSONArray(contenido);

            List<Producto> productos = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                int    id     = obj.getInt("id");
                String nombre = obj.getString("nombre");
                double precio = obj.getDouble("precio");
                int    stock  = obj.getInt("stock");
                productos.add(new Producto(id, nombre, precio, stock, categoria, proveedor));
            }

            System.out.println("Productos cargados desde: " + archivo);
            return productos;

        } catch (Exception e) {
            System.out.println("Error al leer " + archivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ── Guarda / actualiza productos.json ─────────────────────────────────────
    public void guardarProductos(List<Producto> productos) {
        JSONArray array = new JSONArray();

        for (Producto p : productos) {
            JSONObject obj = new JSONObject();
            obj.put("id",     p.getId());
            obj.put("nombre", p.getNombre());
            obj.put("precio", p.getPrecio());
            obj.put("stock",  p.getStock());
            array.put(obj);
        }

        try (FileWriter writer = new FileWriter(archivo, false)) {
            writer.write(array.toString(2)); // 2 = espacios de indentación
            System.out.println("productos.json actualizado.");
        } catch (IOException e) {
            System.out.println("Error guardando productos: " + e.getMessage());
        }
    }
}