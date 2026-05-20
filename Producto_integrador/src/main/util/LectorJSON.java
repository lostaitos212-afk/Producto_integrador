package main.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import main.model.Producto;

public class LectorJSON {
    
    public static List<Producto> leerProductos(String rutaArchivo) {
        List<Producto> lista = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return lista;

        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)), "UTF-8");
            contenido = contenido.trim();
            if(contenido.startsWith("[")) contenido = contenido.substring(1);
            if(contenido.endsWith("]")) contenido = contenido.substring(0, contenido.length()-1);
            if(contenido.isEmpty()) return lista;

            String[] objetos = contenido.split("\\}\\s*,\\s*\\{");
            for (String obj : objetos) {
                String id = extraerString(obj, "\"id\"");
                String nombre = extraerString(obj, "\"nombre\"");
                String cat = extraerString(obj, "\"categoria\"");
                double pCompra = extraerNumero(obj, "\"precioCompra\"");
                double pVenta = extraerNumero(obj, "\"precioVenta\"");
                int stock = (int) extraerNumero(obj, "\"stock\"");
                int minStock = (int) extraerNumero(obj, "\"stockMinimo\"");
                String img = extraerString(obj, "\"rutaImagen\"");

                lista.add(new Producto(id, nombre, pCompra, pVenta, stock, minStock, cat, img));
            }
        } catch (Exception e) {
            ManejadorErrores.registrarError("Error leyendo productos.json", e);
        }
        return lista;
    }

    // NUEVO: Método para guardar los cambios del inventario de vuelta al JSON
    public static void guardarProductos(List<Producto> lista, String rutaArchivo) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < lista.size(); i++) {
            Producto p = lista.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": \"").append(p.getCodigo()).append("\",\n");
            sb.append("    \"nombre\": \"").append(p.getNombre()).append("\",\n");
            sb.append("    \"categoria\": \"").append(p.getCategoria()).append("\",\n");
            sb.append("    \"precioCompra\": ").append(p.getPrecioCompra()).append(",\n");
            sb.append("    \"precioVenta\": ").append(p.getPrecioVenta()).append(",\n");
            sb.append("    \"stock\": ").append(p.getStock()).append(",\n");
            sb.append("    \"stockMinimo\": ").append(p.getLimiteStockMinimo()).append(",\n");
            sb.append("    \"activo\": true,\n");
            // Escapar barras invertidas para las rutas de Windows en el JSON
            sb.append("    \"rutaImagen\": \"").append(p.getImagenRuta().replace("\\", "\\\\")).append("\"\n");
            sb.append("  }");
            if (i < lista.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        try {
            Files.write(Paths.get(rutaArchivo), sb.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            ManejadorErrores.registrarError("Error guardando productos.json", e);
        }
    }

    private static String extraerString(String json, String clave) {
        int indexClave = json.indexOf(clave);
        if(indexClave == -1) return "";
        int indexInicio = json.indexOf("\"", indexClave + clave.length() + 1) + 1;
        int indexFin = json.indexOf("\"", indexInicio);
        return json.substring(indexInicio, indexFin);
    }

    private static double extraerNumero(String json, String clave) {
        int indexClave = json.indexOf(clave);
        if(indexClave == -1) return 0.0;
        int indexDosPuntos = json.indexOf(":", indexClave);
        int indexComa = json.indexOf(",", indexDosPuntos);
        if (indexComa == -1) indexComa = json.indexOf("}", indexDosPuntos);
        if (indexComa == -1) indexComa = json.length();
        String numStr = json.substring(indexDosPuntos + 1, indexComa).trim();
        try { return Double.parseDouble(numStr); } catch(Exception e) { return 0.0; }
    }
}