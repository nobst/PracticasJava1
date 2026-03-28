package Tienda;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Cliente cliente;
    private LocalDate fecha;
    private EstadoPedido estado;
    private List<ItemPedido> items = new ArrayList<>();

    public Pedido(Long id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public void agregarItem(ItemPedido item) {
        items.add(item);
    }

 
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public EstadoPedido getEstado() {
		return estado;
	}

	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
	}

	public List<ItemPedido> getItems() {
		return items;
	}

	public void setItems(List<ItemPedido> items) {
		this.items = items;
	}

	
	
	public double calcularTotal() {
        return items.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
    }
}