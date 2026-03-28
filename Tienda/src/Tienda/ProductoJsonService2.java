package Tienda;
 
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;
 
public class ProductoJsonService2 {
 
    private final String archivo = "productos.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
 
    // Clase auxiliar para serializar/deserializar solo los campos necesarios
    private static class ProductoDto {
        int    id;
        String nombre;
        double precio;
        int    stock;
 
        ProductoDto(Producto p) {
            this.id     = p.getId();
            this.nombre = p.getNombre();
            this.precio = p.getPrecio();
            this.stock  = p.getStock();
        }
    }
 
    // ── Carga productos desde productos.json ──────────────────────────────────
    public List<Producto> cargarProductos(Categoria categoria, Proveedor proveedor) {
        File file = new File(archivo);
 
        if (!file.exists()) {
            System.out.println("Archivo no encontrado. Creando inventario inicial...");
            List<Producto> iniciales = new ArrayList<>();
            iniciales.add(new Producto(1, "HP EliteBook", 3500.0, 10, categoria, proveedor));
            iniciales.add(new Producto(2, "HP ProBook",   2800.0,  5, categoria, proveedor));
            guardarProductos(iniciales);
            return iniciales;
        }
 
        try {
            String contenido = new String(Files.readAllBytes(file.toPath()));
            Type listaTipo = new TypeToken<List<ProductoDto>>() {}.getType();
            List<ProductoDto> dtos = gson.fromJson(contenido, listaTipo);
 
            List<Producto> productos = new ArrayList<>();
            for (ProductoDto dto : dtos) {
                productos.add(new Producto(dto.id, dto.nombre, dto.precio, dto.stock, categoria, proveedor));
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
        List<ProductoDto> dtos = new ArrayList<>();
        for (Producto p : productos) {
            dtos.add(new ProductoDto(p));
        }
 
        try (FileWriter writer = new FileWriter(archivo, false)) {
            gson.toJson(dtos, writer);
            System.out.println("productos.json actualizado.");
        } catch (IOException e) {
            System.out.println("Error guardando productos: " + e.getMessage());
        }
    }
}