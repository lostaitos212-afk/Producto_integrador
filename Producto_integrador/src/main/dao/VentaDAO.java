package main.dao;

import main.model.Venta;
import main.model.DetalleVenta;
import main.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public boolean guardarVenta(Venta venta) {

        String sqlVenta =
                "INSERT INTO ventas " +
                "(folio, subtotal, descuento, total, metodoPago, cambio, fecha, hora) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlDetalle =
                "INSERT INTO detalle_ventas " +
                "(folioVenta, codigoProducto, cantidad, subtotal) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection conn = ConexionBD.getConexion()
        ) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement psVenta =
                            conn.prepareStatement(sqlVenta);

                    PreparedStatement psDetalle =
                            conn.prepareStatement(sqlDetalle)
            ) {

                psVenta.setString(1, venta.getFolio());
                psVenta.setDouble(2, venta.getSubtotal());
                psVenta.setDouble(3, venta.getDescuento());
                psVenta.setDouble(4, venta.getTotal());
                psVenta.setString(5, venta.getMetodoPago());
                psVenta.setDouble(6, venta.getCambio());
                psVenta.setString(7, venta.getFecha());
                psVenta.setString(8, venta.getHora());

                psVenta.executeUpdate();

                for (DetalleVenta d : venta.getItems()) {

                    psDetalle.setString(1, venta.getFolio());
                    psDetalle.setString(2, d.getProducto().getCodigo());
                    psDetalle.setInt(3, d.getCantidad());
                    psDetalle.setDouble(4, d.getSubtotal());

                    psDetalle.executeUpdate();
                }

                conn.commit();

                return true;

            } catch (Exception e) {

                conn.rollback();

                e.printStackTrace();

                return false;
            }

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public List<String> obtenerFolios() {

        List<String> folios =
                new ArrayList<>();

        String sql =
                "SELECT folio FROM ventas ORDER BY fecha DESC, hora DESC";

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                folios.add(
                        rs.getString("folio")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return folios;
    }

    public String obtenerTicketPorFolio(String folio) {

        StringBuilder ticket =
                new StringBuilder();

        String sqlVenta =
                "SELECT * FROM ventas WHERE folio = ?";

        String sqlDetalle =
                "SELECT d.cantidad, d.subtotal, p.nombre " +
                        "FROM detalle_ventas d " +
                        "INNER JOIN productos p " +
                        "ON d.codigoProducto = p.codigo " +
                        "WHERE d.folioVenta = ?";

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement psVenta =
                        conn.prepareStatement(sqlVenta);

                PreparedStatement psDetalle =
                        conn.prepareStatement(sqlDetalle)
        ) {

            psVenta.setString(1, folio);

            ResultSet rsVenta =
                    psVenta.executeQuery();

            if (rsVenta.next()) {

                ticket.append("========================================\n");
                ticket.append("            MEGAPOS EXPRESS             \n");
                ticket.append("========================================\n");

                ticket.append("FOLIO: ")
                        .append(rsVenta.getString("folio"))
                        .append("\n");

                ticket.append("FECHA: ")
                        .append(rsVenta.getString("fecha"))
                        .append("   HORA: ")
                        .append(rsVenta.getString("hora"))
                        .append("\n");

                ticket.append("----------------------------------------\n");

                ticket.append(
                        String.format(
                                "%-5s %-20s %-10s\n",
                                "CANT",
                                "PRODUCTO",
                                "SUBTOTAL"
                        )
                );

                psDetalle.setString(1, folio);

                ResultSet rsDetalle =
                        psDetalle.executeQuery();

                while (rsDetalle.next()) {

                    String nombre =
                            rsDetalle.getString("nombre");

                    if (nombre.length() > 18) {

                        nombre =
                                nombre.substring(0, 18);
                    }

                    ticket.append(
                            String.format(
                                    "%-5d %-20s $%.2f\n",
                                    rsDetalle.getInt("cantidad"),
                                    nombre,
                                    rsDetalle.getDouble("subtotal")
                            )
                    );
                }

                ticket.append("----------------------------------------\n");

                ticket.append(
                        String.format(
                                "SUBTOTAL:                  $%.2f\n",
                                rsVenta.getDouble("subtotal")
                        )
                );

                ticket.append(
                        String.format(
                                "DESCUENTO:                 %.2f%%\n",
                                rsVenta.getDouble("descuento")
                        )
                );

                ticket.append(
                        String.format(
                                "TOTAL NETO:                $%.2f\n",
                                rsVenta.getDouble("total")
                        )
                );

                ticket.append(
                        String.format(
                                "CAMBIO ENTREGADO:          $%.2f\n",
                                rsVenta.getDouble("cambio")
                        )
                );

                ticket.append("MÉTODO DE PAGO: ")
                        .append(rsVenta.getString("metodoPago"))
                        .append("\n");

                ticket.append("========================================\n");
                ticket.append("        GRACIAS POR SU COMPRA!          \n");

            } else {

                ticket.append("No se encontró el ticket.");
            }

        } catch (Exception e) {

            e.printStackTrace();

            ticket.append("Error al cargar ticket desde MySQL.");
        }

        return ticket.toString();
    }

    public String obtenerReportePorFecha(String fecha) {

        StringBuilder reporte =
                new StringBuilder();

        String sql =
                "SELECT folio, total, metodoPago, cambio, hora " +
                        "FROM ventas " +
                        "WHERE fecha = ? " +
                        "ORDER BY hora ASC";

        double totalDia = 0.0;

        int contadorVentas = 0;

        reporte.append("========================================\n");
        reporte.append("          REPORTE DE VENTAS             \n");
        reporte.append("========================================\n");
        reporte.append("FECHA: ").append(fecha).append("\n");
        reporte.append("----------------------------------------\n");

        reporte.append(
                String.format(
                        "%-18s %-10s %-8s\n",
                        "FOLIO",
                        "TOTAL",
                        "HORA"
                )
        );

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, fecha);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                String folio =
                        rs.getString("folio");

                double total =
                        rs.getDouble("total");

                String hora =
                        rs.getString("hora");

                totalDia += total;

                contadorVentas++;

                reporte.append(
                        String.format(
                                "%-18s $%-9.2f %-8s\n",
                                folio,
                                total,
                                hora
                        )
                );
            }

            reporte.append("----------------------------------------\n");

            reporte.append("VENTAS REALIZADAS: ")
                    .append(contadorVentas)
                    .append("\n");

            reporte.append(
                    String.format(
                            "TOTAL VENDIDO: $%.2f\n",
                            totalDia
                    )
            );

            reporte.append("========================================\n");

            if (contadorVentas == 0) {

                reporte.append("No hay ventas registradas en esta fecha.\n");
            }

        } catch (Exception e) {

            e.printStackTrace();

            reporte.append("Error al generar reporte.\n");
        }

        return reporte.toString();
    }

    public String obtenerCSVPorFecha(String fecha) {

        StringBuilder csv =
                new StringBuilder();

        String sql =
                "SELECT folio, subtotal, descuento, total, metodoPago, cambio, fecha, hora " +
                        "FROM ventas " +
                        "WHERE fecha = ? " +
                        "ORDER BY hora ASC";

        csv.append("Folio,Subtotal,Descuento,Total,MetodoPago,Cambio,Fecha,Hora\n");

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, fecha);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                csv.append(rs.getString("folio")).append(",");
                csv.append(rs.getDouble("subtotal")).append(",");
                csv.append(rs.getDouble("descuento")).append(",");
                csv.append(rs.getDouble("total")).append(",");
                csv.append(rs.getString("metodoPago")).append(",");
                csv.append(rs.getDouble("cambio")).append(",");
                csv.append(rs.getString("fecha")).append(",");
                csv.append(rs.getString("hora")).append("\n");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return csv.toString();
    }
}