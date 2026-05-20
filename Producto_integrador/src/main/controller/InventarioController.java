package main.controller;

import main.dao.ProductoDAO;
import main.model.Producto;
import main.view.MainView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InventarioController implements ActionListener {

    private MainView vista;

    private List<Producto> inventario;

    private ProductoDAO dao =
            new ProductoDAO();

    public InventarioController(MainView vista) {

        this.vista = vista;

        cargarInventario();

        cargarCategoriasInventario();

        if (vista.btnInvAgregar != null)
            vista.btnInvAgregar.addActionListener(this);

        if (vista.btnInvModificar != null)
            vista.btnInvModificar.addActionListener(this);

        if (vista.btnInvEliminar != null)
            vista.btnInvEliminar.addActionListener(this);

        if (vista.btnSeleccionarImagen != null)
            vista.btnSeleccionarImagen.addActionListener(this);

        if (vista.btnInvStockMenos != null)
            vista.btnInvStockMenos.addActionListener(this);

        if (vista.btnInvStockMas != null)
            vista.btnInvStockMas.addActionListener(this);

        vista.cmbCategoriaInventario.addActionListener(e -> actualizarTabla());

        vista.txtBuscarInventario.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        actualizarTabla();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        actualizarTabla();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        actualizarTabla();
                    }
                }
        );

        vista.tablaInventario.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        llenarCamposDesdeTabla();
                    }
                }
        );
    }

    private void cargarInventario() {

        inventario =
                dao.obtenerProductos();

        actualizarTabla();

        mostrarAlertaStockBajo();
    }

    private void cargarCategoriasInventario() {

        vista.cmbCategoriaInventario.removeAllItems();

        vista.cmbCategoriaInventario.addItem("TODAS");

        Set<String> categorias =
                new LinkedHashSet<>();

        for (Producto p : inventario) {

            String categoria =
                    p.getCategoria();

            if (categoria != null
                    && !categoria.trim().isEmpty()) {

                categorias.add(categoria.trim());
            }
        }

        for (String c : categorias) {

            vista.cmbCategoriaInventario.addItem(c);
        }
    }

    private void actualizarTabla() {

        vista.modeloInventario.setRowCount(0);

        String busqueda =
                vista.txtBuscarInventario
                        .getText()
                        .trim()
                        .toLowerCase();

        String categoriaFiltro =
                "TODAS";

        if (vista.cmbCategoriaInventario.getSelectedItem() != null) {

            categoriaFiltro =
                    vista.cmbCategoriaInventario
                            .getSelectedItem()
                            .toString();
        }

        for (Producto p : inventario) {

            String codigo =
                    p.getCodigo() == null
                            ? ""
                            : p.getCodigo().toLowerCase();

            String nombre =
                    p.getNombre() == null
                            ? ""
                            : p.getNombre().toLowerCase();

            String categoria =
                    p.getCategoria() == null
                            ? ""
                            : p.getCategoria();

            boolean coincideBusqueda =
                    busqueda.isEmpty()
                            || codigo.contains(busqueda)
                            || nombre.contains(busqueda);

            boolean coincideCategoria =
                    categoriaFiltro.equals("TODAS")
                            || categoria.equals(categoriaFiltro);

            if (!coincideBusqueda || !coincideCategoria) {
                continue;
            }

            String estado;

            if (p.getStock()
                    <= p.getLimiteStockMinimo()) {

                estado = "STOCK INSUFICIENTE";

            } else {

                estado = "Disponible";
            }

            Object[] fila = {
                    p.getCodigo(),
                    p.getNombre(),
                    p.getCategoria(),
                    p.getPrecioVenta(),
                    p.getStock(),
                    estado
            };

            vista.modeloInventario.addRow(fila);
        }
    }

    private void mostrarAlertaStockBajo() {

        StringBuilder alerta =
                new StringBuilder();

        for (Producto p : inventario) {

            if (p.getStock()
                    <= p.getLimiteStockMinimo()) {

                alerta.append("- ")
                        .append(p.getNombre())
                        .append(" | Stock actual: ")
                        .append(p.getStock())
                        .append(" | Mínimo: ")
                        .append(p.getLimiteStockMinimo())
                        .append("\n");
            }
        }

        if (alerta.length() > 0) {

            JOptionPane.showMessageDialog(
                    vista,
                    "ALERTA: productos con stock insuficiente:\n\n"
                            + alerta,
                    "Stock insuficiente",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void llenarCamposDesdeTabla() {

        int fila =
                vista.tablaInventario
                        .getSelectedRow();

        if (fila >= 0) {

            String id =
                    vista.tablaInventario
                            .getValueAt(fila, 0)
                            .toString();

            for (Producto p : inventario) {

                if (p.getCodigo().equals(id)) {

                    vista.txtInvId.setText(
                            p.getCodigo()
                    );

                    vista.txtInvNombre.setText(
                            p.getNombre()
                    );

                    vista.txtInvCategoria.setText(
                            p.getCategoria()
                    );

                    vista.txtInvPrecio.setText(
                            String.valueOf(
                                    p.getPrecioVenta()
                            )
                    );

                    vista.txtInvStock.setText(
                            String.valueOf(
                                    p.getStock()
                            )
                    );

                    if (p.getImagenRuta() != null) {

                        vista.txtInvRutaImagen.setText(
                                p.getImagenRuta()
                        );

                    } else {

                        vista.txtInvRutaImagen.setText("");
                    }

                    break;
                }
            }
        }
    }

    private void cambiarStockInventario(int cambio) {

        try {

            String texto =
                    vista.txtInvStock
                            .getText()
                            .trim();

            int stock;

            if (texto.isEmpty()) {

                stock = 0;

            } else {

                stock =
                        Integer.parseInt(texto);
            }

            stock += cambio;

            if (stock < 0) {

                stock = 0;
            }

            vista.txtInvStock.setText(
                    String.valueOf(stock)
            );

        } catch (NumberFormatException e) {

            vista.txtInvStock.setText("0");
        }
    }

    private void limpiarCampos() {

        vista.txtInvId.setText("");
        vista.txtInvNombre.setText("");
        vista.txtInvCategoria.setText("");
        vista.txtInvPrecio.setText("");
        vista.txtInvStock.setText("");
        vista.txtInvRutaImagen.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()
                == vista.btnInvStockMenos) {

            cambiarStockInventario(-1);
        }

        else if (e.getSource()
                == vista.btnInvStockMas) {

            cambiarStockInventario(1);
        }

        else if (e.getSource()
                == vista.btnSeleccionarImagen) {

            JFileChooser chooser =
                    new JFileChooser();

            int resultado =
                    chooser.showOpenDialog(vista);

            if (resultado
                    == JFileChooser.APPROVE_OPTION) {

                File archivo =
                        chooser.getSelectedFile();

                String rutaRelativa =
                        "Productos/"
                                + archivo.getName();

                vista.txtInvRutaImagen.setText(
                        rutaRelativa
                );
            }
        }

        else if (e.getSource()
                == vista.btnInvAgregar) {

            try {

                String id =
                        vista.txtInvId
                                .getText()
                                .trim();

                String nombre =
                        vista.txtInvNombre
                                .getText()
                                .trim();

                String categoria =
                        vista.txtInvCategoria
                                .getText()
                                .trim();

                String precioTexto =
                        vista.txtInvPrecio
                                .getText()
                                .trim();

                String stockTexto =
                        vista.txtInvStock
                                .getText()
                                .trim();

                String rutaImagen =
                        vista.txtInvRutaImagen
                                .getText()
                                .trim();

                if (id.isEmpty()
                        || nombre.isEmpty()
                        || categoria.isEmpty()
                        || precioTexto.isEmpty()
                        || stockTexto.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "Complete los campos obligatorios."
                    );

                    return;
                }

                for (Producto p : inventario) {

                    if (p.getCodigo().equals(id)) {

                        JOptionPane.showMessageDialog(
                                vista,
                                "Ya existe un producto con ese ID."
                        );

                        return;
                    }
                }

                double precio =
                        Double.parseDouble(precioTexto);

                int stock =
                        Integer.parseInt(stockTexto);

                if (precio < 0 || stock < 0) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "El precio y el stock no pueden ser negativos."
                    );

                    return;
                }

                Producto nuevo =
                        new Producto(
                                id,
                                nombre,
                                0.0,
                                precio,
                                stock,
                                10,
                                categoria,
                                rutaImagen
                        );

                boolean agregado =
                        dao.agregarProducto(nuevo);

                if (agregado) {

                    cargarInventario();
                    cargarCategoriasInventario();

                    limpiarCampos();

                    JOptionPane.showMessageDialog(
                            vista,
                            "Producto agregado correctamente."
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            vista,
                            "No se pudo agregar el producto."
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Precio y stock deben ser numéricos."
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Error al agregar producto."
                );

                ex.printStackTrace();
            }
        }

        else if (e.getSource()
                == vista.btnInvModificar) {

            try {

                String id =
                        vista.txtInvId
                                .getText()
                                .trim();

                String nombre =
                        vista.txtInvNombre
                                .getText()
                                .trim();

                String categoria =
                        vista.txtInvCategoria
                                .getText()
                                .trim();

                String precioTexto =
                        vista.txtInvPrecio
                                .getText()
                                .trim();

                String stockTexto =
                        vista.txtInvStock
                                .getText()
                                .trim();

                String rutaImagen =
                        vista.txtInvRutaImagen
                                .getText()
                                .trim();

                if (id.isEmpty()
                        || nombre.isEmpty()
                        || categoria.isEmpty()
                        || precioTexto.isEmpty()
                        || stockTexto.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "Complete los campos obligatorios."
                    );

                    return;
                }

                double precio =
                        Double.parseDouble(precioTexto);

                int stock =
                        Integer.parseInt(stockTexto);

                if (precio < 0 || stock < 0) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "El precio y el stock no pueden ser negativos."
                    );

                    return;
                }

                Producto modificado =
                        new Producto(
                                id,
                                nombre,
                                0.0,
                                precio,
                                stock,
                                10,
                                categoria,
                                rutaImagen
                        );

                boolean actualizado =
                        dao.actualizarProducto(
                                modificado
                        );

                if (actualizado) {

                    cargarInventario();
                    cargarCategoriasInventario();

                    limpiarCampos();

                    JOptionPane.showMessageDialog(
                            vista,
                            "Producto actualizado correctamente."
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            vista,
                            "No se encontró el producto para actualizar."
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Precio y stock deben ser numéricos."
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Error al modificar producto."
                );

                ex.printStackTrace();
            }
        }

        else if (e.getSource()
                == vista.btnInvEliminar) {

            String id =
                    vista.txtInvId
                            .getText()
                            .trim();

            if (id.isEmpty()) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Ingrese el ID del producto."
                );

                return;
            }

            int confirmacion =
                    JOptionPane.showConfirmDialog(
                            vista,
                            "¿Seguro que deseas eliminar este producto?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmacion
                    == JOptionPane.YES_OPTION) {

                boolean eliminado =
                        dao.eliminarProducto(id);

                if (eliminado) {

                    cargarInventario();
                    cargarCategoriasInventario();

                    limpiarCampos();

                    JOptionPane.showMessageDialog(
                            vista,
                            "Producto eliminado correctamente."
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            vista,
                            "No se encontró el producto."
                    );
                }
            }
        }
    }
}