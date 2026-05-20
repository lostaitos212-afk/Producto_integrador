package main.model;

import java.util.List;

public class Venta {
    private String folio;
    private double subtotal;
    private double descuento; // Porcentaje (ej: 10 para 10%)
    private double total;
    private String metodoPago;
    private double cambio;
    private String fecha;
    private String hora;
    private List<DetalleVenta> items;

    public Venta(String folio, double subtotal, double descuento, double total, String metodoPago, double cambio, String fecha, String hora, List<DetalleVenta> items) {
        this.folio = folio;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.metodoPago = metodoPago;
        this.cambio = cambio;
        this.fecha = fecha;
        this.hora = hora;
        this.items = items;
    }

    public String getFolio() { return folio; }
    public double getSubtotal() { return subtotal; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public String getMetodoPago() { return metodoPago; }
    public double getCambio() { return cambio; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public List<DetalleVenta> getItems() { return items; }
}