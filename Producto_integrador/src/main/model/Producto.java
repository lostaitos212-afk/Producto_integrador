package main.model;

public class Producto {
    private String codigo;
    private String nombre;
    private double precioCompra;
    private double precioVenta;
    private int stock;
    private int limiteStockMinimo;
    private String categoria;
    private String imagenRuta;

    public Producto(String codigo, String nombre, double precioCompra, double precioVenta, int stock, int limiteStockMinimo, String categoria, String imagenRuta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.limiteStockMinimo = limiteStockMinimo;
        this.categoria = categoria;
        this.imagenRuta = imagenRuta;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecioCompra() { return precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrecioVenta(double precioVenta) {
    	this.precioVenta = precioVenta;
    }
    public int getLimiteStockMinimo() { return limiteStockMinimo; }
    public String getCategoria() { return categoria; }
    public String getImagenRuta() { return imagenRuta; }
}