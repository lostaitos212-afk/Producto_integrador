package main.dao;

import main.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CompraDAO {

    public boolean registrarEntradaMercancia(
            String codigoProducto,
            int cantidad,
            double precioCompraUnitario,
            String proveedor
    ) {

        String sqlCompra =
                "INSERT INTO compras " +
                "(proveedor, fecha, hora, total) " +
                "VALUES (?, ?, ?, ?)";

        String sqlDetalle =
                "INSERT INTO detalle_compras " +
                "(idCompra, codigoProducto, cantidad, precioCompraUnitario, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";

        String sqlActualizarProducto =
                "UPDATE productos " +
                "SET stock = stock + ?, precioCompra = ? " +
                "WHERE codigo = ?";

        double total =
                cantidad * precioCompraUnitario;

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
                    PreparedStatement psCompra =
                            conn.prepareStatement(
                                    sqlCompra,
                                    Statement.RETURN_GENERATED_KEYS
                            );

                    PreparedStatement psDetalle =
                            conn.prepareStatement(sqlDetalle);

                    PreparedStatement psActualizar =
                            conn.prepareStatement(sqlActualizarProducto)
            ) {

                psCompra.setString(1, proveedor);
                psCompra.setString(2, fecha);
                psCompra.setString(3, hora);
                psCompra.setDouble(4, total);

                psCompra.executeUpdate();

                ResultSet rs =
                        psCompra.getGeneratedKeys();

                int idCompra = 0;

                if (rs.next()) {

                    idCompra =
                            rs.getInt(1);
                }

                if (idCompra == 0) {

                    conn.rollback();

                    return false;
                }

                psDetalle.setInt(1, idCompra);
                psDetalle.setString(2, codigoProducto);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precioCompraUnitario);
                psDetalle.setDouble(5, total);

                psDetalle.executeUpdate();

                psActualizar.setInt(1, cantidad);
                psActualizar.setDouble(2, precioCompraUnitario);
                psActualizar.setString(3, codigoProducto);

                int filasActualizadas =
                        psActualizar.executeUpdate();

                if (filasActualizadas == 0) {

                    conn.rollback();

                    return false;
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
}