package main.controller;

import main.dao.ProductoDAO;
import main.dao.VentaDAO;

import main.model.Producto;
import main.model.DetalleVenta;
import main.model.Venta;

import main.view.MainView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileWriter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VentaController implements ActionListener {

    private MainView vista;

    private List<Producto> inventarioSimulado;

    private double totalActual = 0.0;

    private ProductoDAO productoDAO =
            new ProductoDAO();

    private VentaDAO ventaDAO =
            new VentaDAO();

    public VentaController(MainView vista) {

        this.vista = vista;

        cargarInventoryDesdeBD();

        cargarCategoriasVentas();

        renderizarGridVisual();

        cargarTicketsDesdeBD();

        if (vista.btnAgregarCarrito != null)
            vista.btnAgregarCarrito.addActionListener(this);

        if (vista.btnCobrar != null)
            vista.btnCobrar.addActionListener(this);

        if (vista.btnVerTicket != null)
            vista.btnVerTicket.addActionListener(this);

        if (vista.btnReporteFecha != null)
            vista.btnReporteFecha.addActionListener(this);

        if (vista.btnExportarReporte != null)
            vista.btnExportarReporte.addActionListener(this);

        if (vista.btnReporteStockBajo != null)
            vista.btnReporteStockBajo.addActionListener(this);

        if (vista.btnQuitarUno != null)
            vista.btnQuitarUno.addActionListener(this);

        if (vista.btnEliminarProducto != null)
            vista.btnEliminarProducto.addActionListener(this);

        if (vista.btnVaciarCarrito != null)
            vista.btnVaciarCarrito.addActionListener(this);

        if (vista.btnCantMenosVenta != null)
            vista.btnCantMenosVenta.addActionListener(this);

        if (vista.btnCantMasVenta != null)
            vista.btnCantMasVenta.addActionListener(this);

        vista.cmbCategoriaVentas.addActionListener(e -> renderizarGridVisual());

        vista.cmbMetodoPago.addActionListener(e -> evaluarMetodoPago());

        vista.txtDescuento.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarTotal();
            }
        });

        vista.txtEfectivo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarCambioDinamico();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarCambioDinamico();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarCambioDinamico();
            }
        });

        vista.txtBuscarProd.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        renderizarGridVisual();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        renderizarGridVisual();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        renderizarGridVisual();
                    }
                }
        );
    }

    private void cargarInventoryDesdeBD() {

        inventarioSimulado =
                productoDAO.obtenerProductos();
    }

    private void cargarCategoriasVentas() {

        vista.cmbCategoriaVentas.removeAllItems();

        vista.cmbCategoriaVentas.addItem("TODAS");

        Set<String> categories =
                new LinkedHashSet<>();

        for (Producto p : inventarioSimulado) {

            String categoria =
                    p.getCategoria();

            if (categoria != null
                    && !categoria.trim().isEmpty()) {

                categories.add(categoria.trim());
            }
        }

        for (String c : categories) {

            vista.cmbCategoriaVentas.addItem(c);
        }
    }

    private void cargarTicketsDesdeBD() {

        vista.cmbTicketsGenerados.removeAllItems();

        List<String> folios =
                ventaDAO.obtenerFolios();

        for (String folio : folios) {

            vista.cmbTicketsGenerados.addItem(folio);
        }
    }

    private void renderizarGridVisual() {

        if (vista.pnlGridProductos == null) {
            return;
        }

        vista.pnlGridProductos.removeAll();

        String textoBusqueda =
                vista.txtBuscarProd
                        .getText()
                        .trim()
                        .toLowerCase();

        String categoriaSeleccionada =
                "TODAS";

        if (vista.cmbCategoriaVentas.getSelectedItem() != null) {

            categoriaSeleccionada =
                    vista.cmbCategoriaVentas
                            .getSelectedItem()
                            .toString();
        }

        for (Producto p : inventarioSimulado) {

            String nombre =
                    p.getNombre() == null
                            ? ""
                            : p.getNombre().toLowerCase();

            String codigo =
                    p.getCodigo() == null
                            ? ""
                            : p.getCodigo().toLowerCase();

            String categoria =
                    p.getCategoria() == null
                            ? ""
                            : p.getCategoria();

            boolean coincideBusqueda =
                    textoBusqueda.isEmpty()
                            || nombre.contains(textoBusqueda)
                            || codigo.contains(textoBusqueda);

            boolean coincideCategoria =
                    categoriaSeleccionada.equals("TODAS")
                            || categoria.equals(categoriaSeleccionada);

            if (!coincideBusqueda || !coincideCategoria) {
                continue;
            }

            MainView.PanelRedondeado tarjeta =
                    vista.new PanelRedondeado(
                            20,
                            vista.COLOR_CARD
                    );

            tarjeta.setLayout(
                    new BoxLayout(
                            tarjeta,
                            BoxLayout.Y_AXIS
                    )
            );

            tarjeta.setBorder(
                    new EmptyBorder(15, 10, 15, 10)
            );

            tarjeta.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            JLabel lblImagen =
                    new JLabel();

            lblImagen.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            lblImagen.setPreferredSize(
                    new Dimension(80, 80)
            );

            lblImagen.setMaximumSize(
                    new Dimension(80, 80)
            );

            String ruta =
                    p.getImagenRuta();

            if (ruta == null) {
                ruta = "";
            }

            File fileImg =
                    new File(
                            System.getProperty("user.dir")
                                    + "/"
                                    + ruta
                    );

            if (fileImg.exists()
                    && !fileImg.isDirectory()) {

                Image img =
                        new ImageIcon(
                                fileImg.getAbsolutePath()
                        )
                                .getImage()
                                .getScaledInstance(
                                        80,
                                        80,
                                        Image.SCALE_SMOOTH
                                );

                lblImagen.setIcon(
                        new ImageIcon(img)
                );

            } else {

                lblImagen.setText("IMG");

                lblImagen.setForeground(
                        vista.COLOR_TEXT_MUTED
                );

                lblImagen.setHorizontalAlignment(
                        SwingConstants.CENTER
                );
            }

            JLabel lblNom =
                    new JLabel(
                            "<html><center>"
                                    + p.getNombre()
                                    + "</center></html>"
                    );

            lblNom.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            11
                    )
            );

            lblNom.setForeground(
                    vista.COLOR_TEXT_LIGHT
            );

            lblNom.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            JLabel lblStock =
                    new JLabel(
                            p.getStock() + " pzas"
                    );

            lblStock.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            10
                    )
            );

            lblStock.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            // MEJORA REQUERIDA: Alerta visual de stock insuficiente o bajo
            if (p.getStock() <= p.getLimiteStockMinimo()) {
                lblStock.setForeground(new Color(239, 68, 68)); // Color rojo de advertencia
                lblStock.setText(p.getStock() + " pzas - ¡STOCK BAJO!");
            } else {
                lblStock.setForeground(vista.COLOR_TEXT_MUTED); // Color gris normal
            }

            JLabel lblPre =
                    new JLabel(
                            String.format(
                                    "$%.2f",
                                    p.getPrecioVenta()
                            )
                    );

            lblPre.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            14
                    )
            );

            lblPre.setForeground(
                    new Color(16, 185, 129)
            );

            lblPre.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            tarjeta.add(lblImagen);
            tarjeta.add(Box.createRigidArea(new Dimension(0, 10)));
            tarjeta.add(lblStock);
            tarjeta.add(Box.createRigidArea(new Dimension(0, 5)));
            tarjeta.add(lblNom);
            tarjeta.add(Box.createRigidArea(new Dimension(0, 5)));
            tarjeta.add(lblPre);

            tarjeta.addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {

                            agregarProductoDirecto(p);
                        }
                    }
            );

            vista.pnlGridProductos.add(tarjeta);
        }

        vista.pnlGridProductos.revalidate();
        vista.pnlGridProductos.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnAgregarCarrito) {

            agregarProductoPorBusqueda();
        }

        else if (e.getSource() == vista.btnCobrar) {

            procesarCobroYGenerarTicket();
        }

        else if (e.getSource() == vista.btnVerTicket) {

            visualizarTicket();
        }

        else if (e.getSource() == vista.btnReporteFecha) {

            generarReportePorFecha();
        }

        else if (e.getSource() == vista.btnExportarReporte) {

            exportarReportePorFecha();
        }

        else if (e.getSource() == vista.btnReporteStockBajo) {

            generarReporteStockBajo();
        }

        else if (e.getSource() == vista.btnQuitarUno) {

            quitarUnaUnidad();
        }

        else if (e.getSource() == vista.btnEliminarProducto) {

            quitarProductoCompleto();
        }

        else if (e.getSource() == vista.btnVaciarCarrito) {

            vaciarCarrito();
        }

        else if (e.getSource() == vista.btnCantMenosVenta) {

            cambiarCantidadVenta(-1);
        }

        else if (e.getSource() == vista.btnCantMasVenta) {

            cambiarCantidadVenta(1);
        }
    }

    private void cambiarCantidadVenta(int cambio) {

        try {

            int cantidad =
                    Integer.parseInt(
                            vista.txtCantidadVenta
                                    .getText()
                                    .trim()
                    );

            box: cantidad += cambio;

            if (cantidad < 1) {
                cantidad = 1;
            }

            vista.txtCantidadVenta.setText(
                    String.valueOf(cantidad)
                );

        } catch (NumberFormatException e) {

            vista.txtCantidadVenta.setText("1");
        }
    }

    private void agregarProductoPorBusqueda() {

        String texto =
                vista.txtBuscarProd
                        .getText()
                        .trim()
                        .toLowerCase();

        if (texto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese código o nombre del producto."
            );

            return;
        }

        for (Producto p : inventarioSimulado) {

            String codigo =
                    p.getCodigo() == null
                            ? ""
                            : p.getCodigo().toLowerCase();

            String nombre =
                    p.getNombre() == null
                            ? ""
                            : p.getNombre().toLowerCase();

            if (codigo.equals(texto)
                    || nombre.contains(texto)) {

                agregarProductoDirecto(p);

                return;
            }
        }

        JOptionPane.showMessageDialog(
                vista,
                "Producto no encontrado."
        );
    }

    private void agregarProductoDirecto(Producto p) {

        int cantidad;

        try {

            cantidad =
                    Integer.parseInt(
                            vista.txtCantidadVenta
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Cantidad inválida."
            );

            vista.txtCantidadVenta.setText("1");

            return;
        }

        if (cantidad <= 0) {

            JOptionPane.showMessageDialog(
                    vista,
                    "La cantidad debe ser mayor a 0."
            );

            return;
        }

        if (p.getStock() <= 0) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Sin stock disponible."
            );

            return;
        }

        for (int i = 0;
             i < vista.modeloCarrito.getRowCount();
             i++) {

            String codigoTabla =
                    vista.modeloCarrito
                            .getValueAt(i, 0)
                            .toString();

            if (codigoTabla.equals(p.getCodigo())) {

                int cantidadActual =
                        Integer.parseInt(
                                vista.modeloCarrito
                                        .getValueAt(i, 1)
                                        .toString()
                        );

                int nuevaCantidad =
                        cantidadActual + cantidad;

                if (nuevaCantidad > p.getStock()) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "No hay suficiente stock.\nStock disponible: "
                                    + p.getStock()
                    );

                    return;
                }

                double subtotal =
                        nuevaCantidad
                                * p.getPrecioVenta();

                vista.modeloCarrito.setValueAt(
                        nuevaCantidad,
                        i,
                        1
                );

                vista.modeloCarrito.setValueAt(
                        subtotal,
                        i,
                        4
                );

                actualizarTotal();

                return;
            }
        }

        if (cantidad > p.getStock()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No hay suficiente stock.\nStock disponible: "
                            + p.getStock()
            );

            return;
        }

        Object[] fila = {
                p.getCodigo(),
                cantidad,
                p.getNombre(),
                p.getPrecioVenta(),
                cantidad * p.getPrecioVenta()
        };

        vista.modeloCarrito.addRow(fila);

        actualizarTotal();
    }

    private void actualizarTotal() {

        double subtotalProductos = 0.0;

        for (int i = 0;
             i < vista.modeloCarrito.getRowCount();
             i++) {

            subtotalProductos +=
                    Double.parseDouble(
                            vista.modeloCarrito
                                    .getValueAt(i, 4)
                                    .toString()
                    );
        }

        double descuento = 0.0;
        try {
            String textDesc = vista.txtDescuento.getText().trim();
            if (!textDesc.isEmpty()) {
                descuento = Double.parseDouble(textDesc);
            }
        } catch (NumberFormatException e) {
            // Formato inválido ignorado de forma segura
        }

        totalActual = subtotalProductos - descuento;
        if (totalActual < 0) {
            totalActual = 0.0;
        }

        vista.lblTotal.setText(
                String.format(
                        "$%.2f",
                        totalActual
                )
        );

        if (vista.cmbMetodoPago.getSelectedItem().toString().equals("TARJETA")) {
            vista.txtEfectivo.setText(String.format("%.2f", totalActual).replace(',', '.'));
        }

        actualizarCambioDinamico();
    }

    private void evaluarMetodoPago() {
        String metodo = vista.cmbMetodoPago.getSelectedItem().toString();

        if (metodo.equals("TARJETA")) {
            vista.txtEfectivo.setEnabled(false);
            vista.txtEfectivo.setText(String.format("%.2f", totalActual).replace(',', '.'));
            vista.lblCambio.setText("$0.00");
        } else {
            vista.txtEfectivo.setEnabled(true);
            vista.txtEfectivo.setText("0");
            vista.lblCambio.setText("$0.00");
            vista.txtEfectivo.requestFocus();
        }
    }

    private void actualizarCambioDinamico() {
        if (vista.cmbMetodoPago.getSelectedItem().toString().equals("TARJETA")) {
            vista.lblCambio.setText("$0.00");
            return;
        }

        try {
            String textoEfectivo = vista.txtEfectivo.getText().trim();
            if (textoEfectivo.isEmpty()) {
                vista.lblCambio.setText("$0.00");
                return;
            }

            double efectivo = Double.parseDouble(textoEfectivo);
            if (efectivo >= totalActual) {
                double cambio = efectivo - totalActual;
                vista.lblCambio.setText(String.format("$%.2f", cambio));
            } else {
                vista.lblCambio.setText("Insuficiente");
            }
        } catch (NumberFormatException e) {
            vista.lblCambio.setText("Inválido");
        }
    }

    private void quitarUnaUnidad() {

        int fila =
                vista.tablaCarrito
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un producto del carrito."
            );

            return;
        }

        int cantidad =
                Integer.parseInt(
                        vista.modeloCarrito
                                .getValueAt(fila, 1)
                                .toString()
                );

        double precio =
                Double.parseDouble(
                        vista.modeloCarrito
                                .getValueAt(fila, 3)
                                .toString()
                );

        if (cantidad > 1) {

            cantidad--;

            vista.modeloCarrito.setValueAt(
                    cantidad,
                    fila,
                    1
            );

            vista.modeloCarrito.setValueAt(
                    cantidad * precio,
                    fila,
                    4
            );

        } else {

            vista.modeloCarrito.removeRow(fila);
        }

        actualizarTotal();
    }

    private void quitarProductoCompleto() {

        int fila =
                vista.tablaCarrito
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un producto del carrito."
            );

            return;
        }

        vista.modeloCarrito.removeRow(fila);

        actualizarTotal();
    }

    private void vaciarCarrito() {

        if (vista.modeloCarrito.getRowCount() == 0) {

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        vista,
                        "¿Deseas vaciar todo el carrito?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta == JOptionPane.YES_OPTION) {

            vista.modeloCarrito.setRowCount(0);

            actualizarTotal();
        }
    }

    private Producto buscarProductoPorCodigo(String codigo) {

        for (Producto p : inventarioSimulado) {

            if (p.getCodigo().equals(codigo)) {

                return p;
            }
        }

        return null;
    }

    private void procesarCobroYGenerarTicket() {

        if (totalActual == 0) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No hay productos en el carrito."
            );

            return;
        }

        try {
            String metodoPago = vista.cmbMetodoPago.getSelectedItem().toString();
            double efectivo = 0.0;
            double cambio = 0.0;

            double descuento = 0.0;
            try {
                String textDesc = vista.txtDescuento.getText().trim();
                if (!textDesc.isEmpty()) {
                    descuento = Double.parseDouble(textDesc);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Monto de descuento inválido.");
                return;
            }

            double subtotalAcumulado = 0.0;
            for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
                subtotalAcumulado += Double.parseDouble(vista.modeloCarrito.getValueAt(i, 4).toString());
            }

            if (metodoPago.equals("EFECTIVO")) {
                efectivo = Double.parseDouble(vista.txtEfectivo.getText().trim());

                if (efectivo < totalActual) {
                    JOptionPane.showMessageDialog(
                            vista,
                            "Efectivo insuficiente."
                    );
                    return;
                }
                cambio = efectivo - totalActual;
            } else {
                efectivo = totalActual;
                cambio = 0.0;
            }

            List<DetalleVenta> detalles =
                    new ArrayList<>();

            for (int i = 0;
                 i < vista.modeloCarrito.getRowCount();
                 i++) {

                String codigo =
                        vista.modeloCarrito
                                .getValueAt(i, 0)
                                .toString();

                int cantidad =
                        Integer.parseInt(
                                vista.modeloCarrito
                                        .getValueAt(i, 1)
                                        .toString()
                        );

                Producto producto =
                        buscarProductoPorCodigo(codigo);

                if (producto == null) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "Producto no encontrado en inventario: "
                                    + codigo
                    );

                    return;
                }

                if (cantidad > producto.getStock()) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "Stock insuficiente para "
                                    + producto.getNombre()
                                    + "\nStock disponible: "
                                    + producto.getStock()
                    );

                    return;
                }

                detalles.add(
                        new DetalleVenta(
                                producto,
                                cantidad
                        )
                );
            }

            String folio =
                    "TK-" + System.currentTimeMillis();

            LocalDate fechaActual =
                    LocalDate.now();

            LocalTime horaActual =
                    LocalTime.now();

            String fecha =
                    fechaActual.format(
                            DateTimeFormatter.ofPattern(
                                    "dd/MM/yyyy"
                            )
                    );

            String hora =
                    horaActual.format(
                            DateTimeFormatter.ofPattern(
                                    "HH:mm:ss"
                            )
                    );

            Venta venta =
                    new Venta(
                            folio,
                            subtotalAcumulado,
                            descuento,
                            totalActual,
                            metodoPago,
                            cambio,
                            fecha,
                            hora,
                            detalles
                    );

            boolean guardado =
                    ventaDAO.guardarVenta(venta);

            if (!guardado) {

                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo guardar la venta en MySQL."
                );

                return;
            }

            for (DetalleVenta d : detalles) {

                productoDAO.descontarStock(
                        d.getProducto().getCodigo(),
                        d.getCantidad()
                );
            }

            vista.cmbTicketsGenerados.addItem(folio);

            vista.txtVisorTicket.setText(
                    ventaDAO.obtenerTicketPorFolio(folio)
            );

            JOptionPane.showMessageDialog(
                    vista,
                    "VENTA EXITOSA\n"
                            + "Ticket: " + folio
                            + "\nMétodo de Pago: " + metodoPago
                            + "\nDescuento Aplicado: $" + String.format("%.2f", descuento)
                            + "\nCambio: $" + String.format("%.2f", cambio)
            );

            vista.modeloCarrito.setRowCount(0);
            totalActual = 0.0;
            vista.lblTotal.setText("$0.00");
            
            vista.cmbMetodoPago.setSelectedIndex(0);
            vista.txtDescuento.setText("0");
            vista.txtEfectivo.setText("0");
            vista.txtEfectivo.setEnabled(true);
            vista.lblCambio.setText("$0.00");

            cargarInventoryDesdeBD();
            cargarCategoriasVentas();
            renderizarGridVisual();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese un monto válido."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al procesar la venta."
            );

            ex.printStackTrace();
        }
    }

    private void visualizarTicket() {

        if (vista.cmbTicketsGenerados
                .getSelectedItem() != null) {

            String folioSelec =
                    vista.cmbTicketsGenerados
                            .getSelectedItem()
                            .toString();

            String ticket =
                    ventaDAO.obtenerTicketPorFolio(
                            folioSelec
                    );

            vista.txtVisorTicket.setText(ticket);
        }
    }

    private void generarReportePorFecha() {

        String fecha =
                vista.txtFechaReporte
                        .getText()
                        .trim();

        if (fecha.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese una fecha."
            );

            return;
        }

        String reporte =
                ventaDAO.obtenerReportePorFecha(
                        fecha
                );

        vista.txtVisorTicket.setText(reporte);
    }

    private void exportarReportePorFecha() {

        String fecha =
                vista.txtFechaReporte
                        .getText()
                        .trim();

        if (fecha.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese una fecha."
            );

            return;
        }

        String csv =
                ventaDAO.obtenerCSVPorFecha(fecha);

        JFileChooser chooser =
                new JFileChooser();

        chooser.setSelectedFile(
                new File("reporte_ventas.csv")
        );

        int opcion =
                chooser.showSaveDialog(vista);

        if (opcion == JFileChooser.APPROVE_OPTION) {

            File archivo =
                    chooser.getSelectedFile();

            try (
                    FileWriter fw =
                            new FileWriter(archivo)
            ) {

                fw.write(csv);

                JOptionPane.showMessageDialog(
                        vista,
                        "Reporte exportado correctamente."
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Error al exportar reporte."
                );

                ex.printStackTrace();
            }
        }
    }

    private void generarReporteStockBajo() {

        List<Producto> bajos =
                productoDAO.obtenerProductosBajoStock();

        StringBuilder reporte =
                new StringBuilder();

        reporte.append("========================================\n");
        reporte.append("        REPORTE DE STOCK BAJO           \n");
        reporte.append("========================================\n");

        reporte.append(
                String.format(
                        "%-8s %-18s %-8s %-8s\n",
                        "CODIGO",
                        "PRODUCTO",
                        "STOCK",
                        "MINIMO"
                )
        );

        reporte.append("----------------------------------------\n");

        if (bajos.isEmpty()) {

            reporte.append("No hay productos con stock bajo.\n");

        } else {

            for (Producto p : bajos) {

                String nombre =
                        p.getNombre();

                if (nombre.length() > 16) {

                    nombre =
                            nombre.substring(0, 16);
                }

                reporte.append(
                        String.format(
                                "%-8s %-18s %-8d %-8d\n",
                                p.getCodigo(),
                                nombre,
                                p.getStock(),
                                p.getLimiteStockMinimo()
                        )
                );
            }
        }

        reporte.append("========================================\n");

        vista.txtVisorTicket.setText(
                reporte.toString()
        );
    }
}