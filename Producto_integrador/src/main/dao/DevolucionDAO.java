package main.dao;

import main.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DevolucionDAO {

    public boolean productoExisteEnVenta(
            String folioVenta,
            String codigoProducto
    ) {

        String sql =
                "SELECT cantidad FROM detalle_ventas " +
                "WHERE folioVenta = ? AND codigoProducto = ?";

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, folioVenta);
            ps.setString(2, codigoProducto);

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public int obtenerCantidadVendida(
            String folioVenta,
            String codigoProducto
    ) {

        String sql =
                "SELECT cantidad FROM detalle_ventas " +
                "WHERE folioVenta = ? AND codigoProducto = ?";

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, folioVenta);
            ps.setString(2, codigoProducto);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("cantidad");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public int obtenerCantidadDevuelta(
            String folioVenta,
            String codigoProducto
    ) {

        String sql =
                "SELECT SUM(cantidad) AS totalDevuelto " +
                "FROM devoluciones " +
                "WHERE folioVenta = ? AND codigoProducto = ?";

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, folioVenta);
            ps.setString(2, codigoProducto);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("totalDevuelto");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public boolean registrarDevolucion(
            String folioVenta,
            String codigoProducto,
            int cantidad,
            String motivo
    ) {

        String sqlInsert =
                "INSERT INTO devoluciones " +
                "(folioVenta, codigoProducto, cantidad, motivo, fecha, hora) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlActualizarStock =
                "UPDATE productos " +
                "SET stock = stock + ? " +
                "WHERE codigo = ?";

        String fecha =
                LocalDate.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                )
                        );

        String hora =
                LocalTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "HH:mm:ss"
                                )
                        );

        try (
                Connection conn =
                        ConexionBD.getConexion()
        ) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement psInsert =
                            conn.prepareStatement(sqlInsert);

                    PreparedStatement psStock =
                            conn.prepareStatement(sqlActualizarStock)
            ) {

                int cantidadVendida =
                        obtenerCantidadVendida(
                                folioVenta,
                                codigoProducto
                        );

                int cantidadDevuelta =
                        obtenerCantidadDevuelta(
                                folioVenta,
                                codigoProducto
                        );

                int cantidadDisponibleParaDevolver =
                        cantidadVendida - cantidadDevuelta;

                if (cantidadVendida == 0) {

                    conn.rollback();

                    return false;
                }

                if (cantidad <= 0
                        || cantidad > cantidadDisponibleParaDevolver) {

                    conn.rollback();

                    return false;
                }

                psInsert.setString(1, folioVenta);
                psInsert.setString(2, codigoProducto);
                psInsert.setInt(3, cantidad);
                psInsert.setString(4, motivo);
                psInsert.setString(5, fecha);
                psInsert.setString(6, hora);

                psInsert.executeUpdate();

                psStock.setInt(1, cantidad);
                psStock.setString(2, codigoProducto);

                psStock.executeUpdate();

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

    public String obtenerResumenDevoluciones() {

        StringBuilder sb =
                new StringBuilder();

        String sql =
                "SELECT d.id, d.folioVenta, d.codigoProducto, p.nombre, " +
                "d.cantidad, d.motivo, d.fecha, d.hora " +
                "FROM devoluciones d " +
                "INNER JOIN productos p ON d.codigoProducto = p.codigo " +
                "ORDER BY d.id DESC";

        sb.append("========================================\n");
        sb.append("          HISTORIAL DEVOLUCIONES        \n");
        sb.append("========================================\n");

        try (
                Connection conn =
                        ConexionBD.getConexion();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            boolean hayDatos =
                    false;

            while (rs.next()) {

                hayDatos = true;

                sb.append("ID: ")
                        .append(rs.getInt("id"))
                        .append("\n");

                sb.append("Folio venta: ")
                        .append(rs.getString("folioVenta"))
                        .append("\n");

                sb.append("Producto: ")
                        .append(rs.getString("codigoProducto"))
                        .append(" - ")
                        .append(rs.getString("nombre"))
                        .append("\n");

                sb.append("Cantidad devuelta: ")
                        .append(rs.getInt("cantidad"))
                        .append("\n");

                sb.append("Motivo: ")
                        .append(rs.getString("motivo"))
                        .append("\n");

                sb.append("Fecha: ")
                        .append(rs.getString("fecha"))
                        .append("  Hora: ")
                        .append(rs.getString("hora"))
                        .append("\n");

                sb.append("----------------------------------------\n");
            }

            if (!hayDatos) {

                sb.append("No hay devoluciones registradas.\n");
            }

        } catch (Exception e) {

            e.printStackTrace();

            sb.append("Error al cargar devoluciones.\n");
        }

        return sb.toString();
    }
}