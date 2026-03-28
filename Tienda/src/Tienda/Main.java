package Tienda;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventarioService service       = new InventarioService();
        PedidoJsonService pedidoService = new PedidoJsonService();
        ProductoJsonService productoService = new ProductoJsonService();

        // Categoría y proveedor base (se usan al crear inventario inicial)
        Categoria laptops = new Categoria(1, "Laptops");
        Proveedor hp      = new Proveedor(1, "HP", "USA", "ventas@hp.com");

        // ── Cargar productos desde productos.json al iniciar ─────────────────
        List<Producto> productos = productoService.cargarProductos(laptops, hp);

        Cliente cliente = new Cliente(1, "Carlos", "carlos@email.com", "Bogotá");

        int opcion;
        do {
            System.out.println("\n===== SISTEMA INVENTARIO =====");
            System.out.println("1. Ver productos");
            System.out.println("2. Realizar pedido");
            System.out.println("3. Ver stock bajo");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                    System.out.println("------ PRODUCTOS DISPONIBLES ------");
                    for (Producto p : productos) {
                        System.out.println(p.getId() + " - " + p.getNombre()
                                + " | Precio: $" + p.getPrecio()
                                + " | Stock: "   + p.getStock());
                    }
                    break;

                case 2:
                    Pedido pedido = new Pedido(1L, cliente);

                    System.out.print("Ingrese ID producto: ");
                    long idProd = sc.nextLong();
                    System.out.print("Cantidad: ");
                    int cantidad = sc.nextInt();

                    Producto seleccionado = null;
                    for (Producto p : productos) {
                        if (p.getId() == idProd) {
                            seleccionado = p;
                            break;
                        }
                    }

                    if (seleccionado != null) {
                        pedido.agregarItem(new ItemPedido(seleccionado, cantidad));
                        try {
                            // Confirma el pedido y descuenta el stock en memoria
                            service.confirmarPedido(pedido);

                            // ── Guardar pedido y actualizar productos.json ──
                            pedidoService.guardarPedidoJson(pedido);
                            productoService.guardarProductos(productos);

                            System.out.println("Pedido confirmado. Total: $" + pedido.calcularTotal());
                            System.out.println("Stock actualizado en " + "productos.json");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                    break;

                case 3:
                    System.out.print("Ingrese umbral de stock: ");
                    int umbral = sc.nextInt();
                    List<Producto> bajos = service.productosConStockBajo(productos, umbral);
                    if (bajos.isEmpty()) {
                        System.out.println("No hay productos bajo el umbral " + umbral);
                    } else {
                        System.out.println("------ STOCK BAJO ------");
                        for (Producto p : bajos) {
                            System.out.println(p.getNombre() + " -> Stock: " + p.getStock());
                        }
                    }
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);

        sc.close();
    }
}