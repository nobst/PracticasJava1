package Tienda;

import org.json.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ClienteJsonService {
	
	 private final String archivo = "clientes.json";
	 
	    // ── Carga clientes desde clientes.json ────────────────────────────────────
	    public List<Cliente> cargarClientes() {
	        File file = new File(archivo);
	 
	        if (!file.exists()) {
	            System.out.println("Archivo clientes no encontrado. Creando clientes iniciales...");
	            List<Cliente> iniciales = new ArrayList<>();
	            iniciales.add(new Cliente(1, "Carlos",  "carlos@email.com",  "Bogotá"));
	            iniciales.add(new Cliente(2, "Ana",     "ana@email.com",     "Medellín"));
	            iniciales.add(new Cliente(3, "Luis",    "luis@email.com",    "Cali"));
	            guardarClientes(iniciales);
	            return iniciales;
	        }
	 
	        try {
	            String contenido = new String(Files.readAllBytes(file.toPath()));
	            JSONArray array = new JSONArray(contenido);
	 
	            List<Cliente> clientes = new ArrayList<>();
	            for (int i = 0; i < array.length(); i++) {
	                JSONObject obj = array.getJSONObject(i);
	                int    id     = obj.getInt("id");
	                String nombre = obj.getString("nombre");
	                String correo = obj.getString("correo");
	                String ciudad = obj.getString("ciudad");
	                clientes.add(new Cliente(id, nombre, correo, ciudad));
	            }
	 
	            System.out.println("Clientes cargados desde: " + archivo);
	            return clientes;
	 
	        } catch (Exception e) {
	            System.out.println("Error al leer " + archivo + ": " + e.getMessage());
	            return new ArrayList<>();
	        }
	    }
	 
	    // ── Guarda / actualiza clientes.json ──────────────────────────────────────
	    public void guardarClientes(List<Cliente> clientes) {
	        JSONArray array = new JSONArray();
	 
	        for (Cliente c : clientes) {
	            JSONObject obj = new JSONObject();
	            obj.put("id",     c.getId());
	            obj.put("nombre", c.getNombre());
	            obj.put("correo", c.getCorreo());
	            obj.put("ciudad", c.getCiudad());
	            array.put(obj);
	        }
	 
	        try (FileWriter writer = new FileWriter(archivo, false)) {
	            writer.write(array.toString(2));
	            System.out.println("clientes.json actualizado.");
	        } catch (IOException e) {
	            System.out.println("Error guardando clientes: " + e.getMessage());
	        }
	    }
	 
	    // ── Busca un cliente por ID ───────────────────────────────────────────────
	    public Cliente buscarPorId(List<Cliente> clientes, int id) {
	        for (Cliente c : clientes) {
	            if (c.getId() == id) return c;
	        }
	        return null;
	    }

}
