package Tienda;

public class productos {
	
	private String Nombre;
	private double PrecioVenta;
	private int CantidadStock;
	private int Umbral;

	
	private productos(String Nombre, double PrecioVenta, int CantidadStock) {
		this.Nombre = Nombre;
		this.PrecioVenta = PrecioVenta;
		
		
		if(CantidadStock<0){
			this.CantidadStock = 0;
		}
		else {
			this.CantidadStock = CantidadStock;
		}	
	}


	public String getNombre() {
		return Nombre;
	}


	public void setNombre(String nombre) {
		Nombre = nombre;
	}


	public double getPrecioVenta() {
		return PrecioVenta;
	}


	public void setPrecioVenta(double precioVenta) {
		PrecioVenta = precioVenta;
	}


	public int getCantidadStock() {
		return CantidadStock;
	}


	public void setCantidadStock(int cantidadStock) {
		CantidadStock = cantidadStock;
	}
	
	
	
}


