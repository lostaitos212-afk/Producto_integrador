package main.dao;

import main.model.Producto;
import main.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> obtenerProductos() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos";

        try (
                Connection conn = ConexionBD.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Producto p = new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precioCompra"),
                        rs.getDouble("precioVenta"),
                        rs.getInt("stock"),
                        rs.getInt("stockMinimo"),
                        rs.getString("categoria"),
                        rs.getString("rutaImagen")
                );

                lista.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public boolean agregarProducto(Producto p) {

        String sql =
                "INSERT INTO productos " +
                "(codigo, nombre, precioCompra, precioVenta, stock, stockMinimo, categoria, rutaImagen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = ConexionBD.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setDouble(3, p.getPrecioCompra());
            ps.setDouble(4, p.getPrecioVenta());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getLimiteStockMinimo());
            ps.setString(7, p.getCategoria());
            ps.setString(8, p.getImagenRuta());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean actualizarProducto(Producto p) {

        String sql =
                "UPDATE productos " +
                "SET nombre = ?, precioVenta = ?, stock = ?, categoria = ?, rutaImagen = ? " +
                "WHERE codigo = ?";

        try (
                Connection conn = ConexionBD.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecioVenta());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getCategoria());
            ps.setString(5, p.getImagenRuta());
            ps.setString(6, p.getCodigo());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean eliminarProducto(String codigo) {

        String sql =
                "DELETE FROM productos WHERE codigo = ?";

        try (
                Connection conn = ConexionBD.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, codigo);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public void descontarStock(String codigo, int cantidad) {

        String sql =
                "UPDATE productos " +
                "SET stock = stock - ? " +
                "WHERE codigo = ?";

        try (
                Connection conn = ConexionBD.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, cantidad);
            ps.setString(2, codigo);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<Producto> obtenerProductosBajoStock() {

        List<Producto> lista = new ArrayList<>();

        String sql =
                "SELECT * FROM productos " +
                "WHERE stock <= stockMinimo " +
                "ORDER BY stock ASC";

        try (
                Connection conn = ConexionBD.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Producto p = new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precioCompra"),
                        rs.getDouble("precioVenta"),
                        rs.getInt("stock"),
                        rs.getInt("stockMinimo"),
                        rs.getString("categoria"),
                        rs.getString("rutaImagen")
                );

                lista.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
}