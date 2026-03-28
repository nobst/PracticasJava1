package Tienda;

import java.util.*;

public class Main {

    static long contadorPedido = 1;

    public static void main(String[] args) {
    	
        Scanner sc = new Scanner(System.in);
        InventarioService   service         = new InventarioService();
        PedidoJsonService   pedidoService   = new PedidoJsonService();
        ProductoJsonService productoService = new ProductoJsonService();
        ClienteJsonService  clienteService  = new ClienteJsonService();

        Categoria laptops = new Categoria(1, "Laptops");
        Proveedor hp      = new Proveedor(1, "HP", "USA", "ventas@hp.com");

        // ── Cargar datos desde JSON al iniciar ───────────────────────────────
        List<Producto> productos = productoService.cargarProductos(laptops, hp);
        List<Cliente>  clientes  = clienteService.cargarClientes();

        int opcion;
        do {
            System.out.println("\n===== SISTEMA INVENTARIO =====");
            System.out.println("1. Ver productos");
            System.out.println("2. Ver clientes");
            System.out.println("3. Realizar pedido");
            System.out.println("4. Ver stock bajo");
            System.out.println("5. Historial de pedidos de un cliente");
            System.out.println("6. Reporte general de ventas");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                    System.out.println("------ PRODUCTOS DISPONIBLES ------");
                    for (Producto p : productos) {
                        System.out.printf("%-3d %-25s | Precio: $%-8.2f | Stock: %d%n",
                                p.getId(), p.getNombre(), p.getPrecio(), p.getStock());
                    }
                    break;

                case 2:
                    System.out.println("------ CLIENTES REGISTRADOS ------");
                    for (Cliente c : clientes) {
                        System.out.printf("%-3d %-20s | %s | %s%n",
                                c.getId(), c.getNombre(), c.getCorreo(), c.getCiudad());
                    }
                    break;

                case 3:
                    // Mostrar clientes
                    System.out.println("------ CLIENTES ------");
                    for (Cliente c : clientes) {
                        System.out.println(c.getId() + " - " + c.getNombre());
                    }
                    System.out.print("Ingrese ID cliente: ");
                    int idCliente = sc.nextInt();
                    Cliente clienteSeleccionado = clienteService.buscarPorId(clientes, idCliente);

                    if (clienteSeleccionado == null) {
                        System.out.println("Cliente no encontrado.");
                        break;
                    }

                    // Mostrar productos
                    System.out.println("------ PRODUCTOS ------");
                    for (Producto p : productos) {
                        System.out.printf("%-3d %-25s | Stock: %d%n",
                                p.getId(), p.getNombre(), p.getStock());
                    }
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
                        Pedido pedido = new Pedido(contadorPedido++, clienteSeleccionado);
                        pedido.agregarItem(new ItemPedido(seleccionado, cantidad));
                        try {
                            // 1. Confirmar y descontar stock en memoria
                            service.confirmarPedido(pedido);
                            // 2. Guardar pedido en pedidos.json (acumula)
                            pedidoService.guardarPedidoJson(pedido);
                            // 3. Actualizar stock en productos.json
                            productoService.guardarProductos(productos);

                            System.out.println("Pedido confirmado para: " + clienteSeleccionado.getNombre());
                            System.out.printf("Total: $%.2f%n", pedido.calcularTotal());
                            System.out.println("Stock de '" + seleccionado.getNombre()
                                    + "' actualizado a: " + seleccionado.getStock());
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Ingrese umbral de stock: ");
                    int umbral = sc.nextInt();
                    List<Producto> bajos = service.productosConStockBajo(productos, umbral);
                    if (bajos.isEmpty()) {
                        System.out.println("No hay productos bajo el umbral " + umbral);
                    } else {
                        System.out.println("------ STOCK BAJO ------");
                        for (Producto p : bajos) {
                            System.out.printf("%-25s -> Stock: %d%n", p.getNombre(), p.getStock());
                        }
                    }
                    break;

                case 5:
                    System.out.println("------ CLIENTES ------");
                    for (Cliente c : clientes) {
                        System.out.println(c.getId() + " - " + c.getNombre());
                    }
                    System.out.print("Ingrese ID cliente: ");
                    int idBuscar = sc.nextInt();
                    Cliente clienteBuscado = clienteService.buscarPorId(clientes, idBuscar);

                    if (clienteBuscado == null) {
                        System.out.println("Cliente no encontrado.");
                        break;
                    }

                    pedidoService.mostrarHistorialCliente(idBuscar, clienteBuscado.getNombre());
                    break;

                case 6:
                    pedidoService.generarReporteVentas();
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