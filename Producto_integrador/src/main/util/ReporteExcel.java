package main.util;

import main.model.Venta;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReporteExcel {
    public static void exportarVentas(List<Venta> ventas, String rutaArchivo) throws IOException {
        // Generaremos un CSV en lugar de un XLSX para evitar depender de Apache POI.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            // Encabezado
            bw.write("Folio,Fecha,Hora,Subtotal,Descuento(%),Total");
            bw.newLine();

            for (Venta venta : ventas) {
                String linea = String.format("%s,%s,%s,%.2f,%.2f,%.2f",
                        venta.getFolio(), venta.getFecha(), venta.getHora(),
                        venta.getSubtotal(), venta.getDescuento(), venta.getTotal());
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}