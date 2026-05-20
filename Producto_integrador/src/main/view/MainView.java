package main.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MainView extends JFrame {

    public CardLayout cardLayout;
    public JPanel contentPanel;

    // --- MÓDULO VENTAS ---
    public JTextField txtBuscarProd = new JTextField();

    public JComboBox<String> cmbCategoriaVentas = new JComboBox<>();

    public JTextField txtCantidadVenta = new JTextField("1");

    public JButton btnCantMenosVenta =
            new BotonPildora("-", new Color(239, 68, 68), Color.WHITE);

    public JButton btnCantMasVenta =
            new BotonPildora("+", new Color(16, 185, 129), Color.WHITE);

    public JButton btnAgregarCarrito =
            new BotonPildora("AGREGAR", new Color(59, 130, 246), Color.WHITE);

    public DefaultTableModel modeloCarrito =
            new DefaultTableModel(
                    new String[]{"Código", "Cant", "Producto", "Precio", "Subtotal"},
                    0
            );

    public JTable tablaCarrito = new JTable(modeloCarrito);

    public JLabel lblTotal = new JLabel("$0.00", SwingConstants.CENTER);

    public JTextField txtEfectivo = new JTextField("0");
    
    // COMPONENTES PÚBLICOS PARA EL CONTROL DE PAGOS Y DESCUENTOS
    public JComboBox<String> cmbMetodoPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA"});
    public JTextField txtDescuento = new JTextField("0"); // NUEVO CAMPO DE DESCUENTO
    public JLabel lblCambio = new JLabel("$0.00", SwingConstants.CENTER);

    public JButton btnCobrar =
            new BotonPildora("PAGAR Y GENERAR TICKET", new Color(16, 185, 129), Color.WHITE);

    public JButton btnEliminarProducto =
            new BotonPildora("ELIMINAR PRODUCTO", new Color(239, 68, 68), Color.WHITE);

    public JButton btnQuitarUno =
            new BotonPildora("QUITAR 1", new Color(245, 158, 11), Color.WHITE);

    public JButton btnVaciarCarrito =
            new BotonPildora("VACIAR CARRITO", new Color(100, 116, 139), Color.WHITE);

    public JPanel pnlGridProductos;

    // --- MÓDULO INVENTARIO ---
    public JTextField txtBuscarInventario = new JTextField();

    public JComboBox<String> cmbCategoriaInventario = new JComboBox<>();

    public DefaultTableModel modeloInventario =
            new DefaultTableModel(
                    new String[]{"ID", "Nombre", "Categoría", "Precio Venta", "Stock", "Estado"},
                    0
            );

    public JTable tablaInventario = new JTable(modeloInventario);

    public JTextField txtInvId = new JTextField();
    public JTextField txtInvNombre = new JTextField();
    public JTextField txtInvCategoria = new JTextField();
    public JTextField txtInvPrecio = new JTextField();
    public JTextField txtInvStock = new JTextField();
    public JTextField txtInvRutaImagen = new JTextField();

    public JButton btnInvStockMenos =
            new BotonPildora("-", new Color(239, 68, 68), Color.WHITE);

    public JButton btnInvStockMas =
            new BotonPildora("+", new Color(16, 185, 129), Color.WHITE);

    public JButton btnSeleccionarImagen =
            new BotonPildora("SELECCIONAR IMAGEN", new Color(59, 130, 246), Color.WHITE);

    public JButton btnInvAgregar =
            new BotonPildora("NUEVO", new Color(16, 185, 129), Color.WHITE);

    public JButton btnInvModificar =
            new BotonPildora("GUARDAR", new Color(59, 130, 246), Color.WHITE);

    public JButton btnInvEliminar =
            new BotonPildora("ELIMINAR", new Color(239, 68, 68), Color.WHITE);

    // --- MÓDULO COMPRAS / ENTRADA MERCANCÍA ---
    public JTextField txtCompraCodigo = new JTextField();
    public JTextField txtCompraProveedor = new JTextField();
    public JTextField txtCompraCantidad = new JTextField("1");
    public JTextField txtCompraPrecio = new JTextField();

    public JButton btnCompraMenos =
            new BotonPildora("-", new Color(239, 68, 68), Color.WHITE);

    public JButton btnCompraMas =
            new BotonPildora("+", new Color(16, 185, 129), Color.WHITE);

    public JButton btnRegistrarCompra =
            new BotonPildora("REGISTRAR ENTRADA", new Color(16, 185, 129), Color.WHITE);

    public JTextArea txtResultadoCompra = new JTextArea();

    // --- MÓDULO AUDITORÍA TICKETS ---
    public JComboBox<String> cmbTicketsGenerados = new JComboBox<>();
    public JTextArea txtVisorTicket = new JTextArea();

    public JButton btnVerTicket =
            new BotonPildora("VISUALIZAR TICKET", new Color(59, 130, 246), Color.WHITE);

    // --- MÓDULO REPORTES ---
    public JTextField txtFechaReporte = new JTextField();

    public JButton btnReporteFecha =
            new BotonPildora("REPORTE POR FECHA", new Color(16, 185, 129), Color.WHITE);

    public JButton btnExportarReporte =
            new BotonPildora("EXPORTAR REPORTE", new Color(245, 158, 11), Color.WHITE);

    public JButton btnReporteStockBajo =
            new BotonPildora("STOCK BAJO", new Color(239, 68, 68), Color.WHITE);

    // Sidebar
    public JButton btnNavVentas;
    public JButton btnNavInventario;
    public JButton btnNavCompras;
    public JButton btnNavAuditoria;
    public JButton btnNavReportes;

    // Colores
    public final Color COLOR_BG = new Color(8, 10, 18);
    public final Color COLOR_CARD = new Color(15, 23, 42);
    public final Color COLOR_TEXT_MUTED = new Color(148, 163, 184);
    public final Color COLOR_TEXT_LIGHT = new Color(248, 250, 252);

    public MainView() {
        setTitle("MEGAPOS - Enterprise v5.3");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        ensamblarInterfaz();
    }

    private void ensamblarInterfaz() {
        add(crearSidebar(), BorderLayout.WEST);

        JPanel centroPanel = new JPanel(new BorderLayout());
        centroPanel.setBackground(COLOR_BG);

        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setPreferredSize(new Dimension(0, 60));
        topbar.setBackground(COLOR_CARD);

        topbar.setBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0,
                        new Color(30, 41, 59)
                )
        );

        JLabel lblUser = new JLabel(" Operador Master: ADMIN");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUser.setForeground(COLOR_TEXT_LIGHT);
        lblUser.setBorder(new EmptyBorder(0, 20, 0, 0));

        topbar.add(lblUser, BorderLayout.WEST);
        centroPanel.add(topbar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(COLOR_BG);

        contentPanel.add(construirPanelVentas(), "VENTAS");
        contentPanel.add(construirPanelInventario(), "INVENTARIO");
        contentPanel.add(construirPanelCompras(), "COMPRAS");
        contentPanel.add(construirPanelAuditoriaTickets(), "AUDITORIA");
        contentPanel.add(construirPanelReportes(), "REPORTES");

        centroPanel.add(contentPanel, BorderLayout.CENTER);
        add(centroPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "VENTAS");
    }

    private JPanel crearSidebar() {
        JPanel s = new JPanel();
        s.setPreferredSize(new Dimension(220, 0));
        s.setBackground(COLOR_CARD);
        s.setLayout(new BoxLayout(s, BoxLayout.Y_AXIS));
        s.setBorder(new EmptyBorder(25, 15, 25, 15));

        JLabel lblLogo =
                new JLabel("<html><b style='color:#FFF; font-size:18px;'>MEGA</b><b style='color:#10B981; font-size:18px;'>POS</b></html>");

        s.add(lblLogo);
        s.add(Box.createRigidArea(new Dimension(0, 40)));

        btnNavVentas =
                new BotonPildora("  Ventas", new Color(16, 185, 129), Color.WHITE);

        btnNavInventario =
                new BotonPildora("  Inventario", COLOR_CARD, COLOR_TEXT_MUTED);

        btnNavCompras =
                new BotonPildora("  Compras", COLOR_CARD, COLOR_TEXT_MUTED);

        btnNavAuditoria =
                new BotonPildora("  Auditoría Tickets", COLOR_CARD, COLOR_TEXT_MUTED);

        btnNavReportes =
                new BotonPildora("  Reportes", COLOR_CARD, COLOR_TEXT_MUTED);

        Dimension btnSize = new Dimension(200, 40);

        btnNavVentas.setMaximumSize(btnSize);
        btnNavInventario.setMaximumSize(btnSize);
        btnNavCompras.setMaximumSize(btnSize);
        btnNavAuditoria.setMaximumSize(btnSize);
        btnNavReportes.setMaximumSize(btnSize);

        btnNavVentas.setHorizontalAlignment(SwingConstants.LEFT);
        btnNavInventario.setHorizontalAlignment(SwingConstants.LEFT);
        btnNavCompras.setHorizontalAlignment(SwingConstants.LEFT);
        btnNavAuditoria.setHorizontalAlignment(SwingConstants.LEFT);
        btnNavReportes.setHorizontalAlignment(SwingConstants.LEFT);

        s.add(btnNavVentas);
        s.add(Box.createRigidArea(new Dimension(0, 10)));
        s.add(btnNavInventario);
        s.add(Box.createRigidArea(new Dimension(0, 10)));
        s.add(btnNavCompras);
        s.add(Box.createRigidArea(new Dimension(0, 10)));
        s.add(btnNavAuditoria);
        s.add(Box.createRigidArea(new Dimension(0, 10)));
        s.add(btnNavReportes);

        btnNavVentas.addActionListener(e -> {
            activarBoton(btnNavVentas);
            cardLayout.show(contentPanel, "VENTAS");
        });

        btnNavInventario.addActionListener(e -> {
            activarBoton(btnNavInventario);
            cardLayout.show(contentPanel, "INVENTARIO");
        });

        btnNavCompras.addActionListener(e -> {
            activarBoton(btnNavCompras);
            cardLayout.show(contentPanel, "COMPRAS");
        });

        btnNavAuditoria.addActionListener(e -> {
            activarBoton(btnNavAuditoria);
            cardLayout.show(contentPanel, "AUDITORIA");
        });

        btnNavReportes.addActionListener(e -> {
            activarBoton(btnNavReportes);
            cardLayout.show(contentPanel, "REPORTES");
        });

        return s;
    }

    private void activarBoton(JButton activo) {
        btnNavVentas.setBackground(COLOR_CARD);
        btnNavVentas.setForeground(COLOR_TEXT_MUTED);

        btnNavInventario.setBackground(COLOR_CARD);
        btnNavInventario.setForeground(COLOR_TEXT_MUTED);

        btnNavCompras.setBackground(COLOR_CARD);
        btnNavCompras.setForeground(COLOR_TEXT_MUTED);

        btnNavAuditoria.setBackground(COLOR_CARD);
        btnNavAuditoria.setForeground(COLOR_TEXT_MUTED);

        btnNavReportes.setBackground(COLOR_CARD);
        btnNavReportes.setForeground(COLOR_TEXT_MUTED);

        activo.setBackground(new Color(16, 185, 129));
        activo.setForeground(Color.WHITE);
    }

    private JPanel construirPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topBusqueda = new JPanel(new BorderLayout(10, 0));
        topBusqueda.setOpaque(false);

        txtBuscarProd.setBackground(COLOR_CARD);
        txtBuscarProd.setForeground(COLOR_TEXT_LIGHT);
        txtBuscarProd.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(51, 65, 85)),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );

        JPanel pnlIzquierdaBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlIzquierdaBusqueda.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar producto:");
        lblBuscar.setForeground(COLOR_TEXT_LIGHT);
        lblBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtBuscarProd.setPreferredSize(new Dimension(280, 35));
        pnlIzquierdaBusqueda.add(lblBuscar);
        pnlIzquierdaBusqueda.add(txtBuscarProd);

        topBusqueda.add(pnlIzquierdaBusqueda, BorderLayout.WEST);

        JPanel pnlDerechaBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        pnlDerechaBusqueda.setOpaque(false);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setForeground(COLOR_TEXT_LIGHT);
        lblCantidad.setFont(new Font("SansSerif", Font.BOLD, 13));

        cmbCategoriaVentas.setPreferredSize(new Dimension(150, 35));

        JPanel controlCantidadVenta =
                crearControlCantidad(
                        txtCantidadVenta,
                        btnCantMenosVenta,
                        btnCantMasVenta
                );

        pnlDerechaBusqueda.add(cmbCategoriaVentas);
        pnlDerechaBusqueda.add(lblCantidad);
        pnlDerechaBusqueda.add(controlCantidadVenta);
        pnlDerechaBusqueda.add(btnAgregarCarrito);

        topBusqueda.add(pnlDerechaBusqueda, BorderLayout.EAST);

        pnlGridProductos = new JPanel(new GridLayout(0, 4, 15, 15));
        pnlGridProductos.setBackground(COLOR_BG);

        JScrollPane scrollGrid = new JScrollPane(pnlGridProductos);
        scrollGrid.setBorder(null);
        scrollGrid.getViewport().setBackground(COLOR_BG);

        tablaCarrito.setFillsViewportHeight(true);
        tablaCarrito.setBackground(COLOR_CARD);
        tablaCarrito.setForeground(COLOR_TEXT_LIGHT);
        tablaCarrito.setRowHeight(35);

        tablaCarrito.getTableHeader().setBackground(new Color(30, 41, 59));
        tablaCarrito.getTableHeader().setForeground(COLOR_TEXT_LIGHT);

        JScrollPane tableScroll = new JScrollPane(tablaCarrito);

        PanelRedondeado panelCobro = new PanelRedondeado(25, COLOR_CARD);
        panelCobro.setPreferredSize(new Dimension(340, 0));
        panelCobro.setLayout(new BorderLayout(0, 15));
        panelCobro.setBorder(new EmptyBorder(25, 20, 25, 20));

        JPanel pnlDatosCobro = new JPanel();
        pnlDatosCobro.setOpaque(false);
        pnlDatosCobro.setLayout(new BoxLayout(pnlDatosCobro, BoxLayout.Y_AXIS));

        JLabel lblTit = new JLabel("TOTAL A PAGAR", SwingConstants.CENTER);
        lblTit.setForeground(COLOR_TEXT_MUTED);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTotal.setForeground(Color.WHITE);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        cmbMetodoPago.setBackground(new Color(30, 41, 59));
        cmbMetodoPago.setForeground(Color.WHITE);
        cmbMetodoPago.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(51, 65, 85)),
                        "Método de Pago", 0, 0, null, COLOR_TEXT_MUTED
                )
        );

        // ESTILOS PARA EL NUEVO CAMPO DE DESCUENTO
        txtDescuento.setBackground(new Color(30, 41, 59));
        txtDescuento.setForeground(Color.WHITE);
        txtDescuento.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(51, 65, 85)),
                        "Descuento ($)", 0, 0, null, COLOR_TEXT_MUTED
                )
        );

        txtEfectivo.setBackground(new Color(30, 41, 59));
        txtEfectivo.setForeground(Color.WHITE);
        txtEfectivo.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(51, 65, 85)),
                        "Efectivo Recibido", 0, 0, null, COLOR_TEXT_MUTED
                )
        );

        lblCambio.setForeground(new Color(16, 185, 129));
        lblCambio.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblCambio.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(51, 65, 85)),
                        "Cambio", 0, 0, null, COLOR_TEXT_MUTED
                )
        );

        pnlDatosCobro.add(lblTit);
        pnlDatosCobro.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlDatosCobro.add(lblTotal);
        pnlDatosCobro.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlDatosCobro.add(cmbMetodoPago);
        pnlDatosCobro.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlDatosCobro.add(txtDescuento); // SE ACOMODA EN EL PANEL
        pnlDatosCobro.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlDatosCobro.add(txtEfectivo);
        pnlDatosCobro.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlDatosCobro.add(lblCambio);

        panelCobro.add(pnlDatosCobro, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new GridLayout(4, 1, 0, 10));
        pnlBotones.setOpaque(false);

        pnlBotones.add(btnCobrar);
        pnlBotones.add(btnQuitarUno);
        pnlBotones.add(btnEliminarProducto);
        pnlBotones.add(btnVaciarCarrito);

        panelCobro.add(pnlBotones, BorderLayout.SOUTH);

        JPanel pnlIzquierdo = new JPanel(new BorderLayout(0, 15));
        pnlIzquierdo.setOpaque(false);

        JSplitPane splitVertical =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        scrollGrid,
                        tableScroll
                );

        splitVertical.setDividerLocation(300);
        splitVertical.setDividerSize(0);
        splitVertical.setBorder(null);
        splitVertical.setOpaque(false);

        pnlIzquierdo.add(topBusqueda, BorderLayout.NORTH);
        pnlIzquierdo.add(splitVertical, BorderLayout.CENTER);

        panel.add(pnlIzquierdo, BorderLayout.CENTER);
        panel.add(panelCobro, BorderLayout.EAST);

        return panel;
    }

    private JPanel construirPanelInventario() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topInventario = new JPanel(new BorderLayout(10, 0));
        topInventario.setOpaque(false);

        txtBuscarInventario.setBackground(COLOR_CARD);
        txtBuscarInventario.setForeground(COLOR_TEXT_LIGHT);
        txtBuscarInventario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(51, 65, 85)),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );

        cmbCategoriaInventario.setPreferredSize(new Dimension(180, 35));

        topInventario.add(txtBuscarInventario, BorderLayout.CENTER);
        topInventario.add(cmbCategoriaInventario, BorderLayout.EAST);

        tablaInventario.setFillsViewportHeight(true);
        tablaInventario.setBackground(COLOR_CARD);
        tablaInventario.setForeground(COLOR_TEXT_LIGHT);
        tablaInventario.getTableHeader().setBackground(new Color(30, 41, 59));
        tablaInventario.getTableHeader().setForeground(COLOR_TEXT_LIGHT);

        JScrollPane scroll = new JScrollPane(tablaInventario);

        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setOpaque(false);
        centro.add(topInventario, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        PanelRedondeado form = new PanelRedondeado(20, COLOR_CARD);
        form.setPreferredSize(new Dimension(310, 0));
        form.setLayout(new GridLayout(15, 1, 0, 8));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("GESTIÓN PRODUCTO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));

        form.add(titulo);
        form.add(crearInputInv("ID / Código", txtInvId));
        form.add(crearInputInv("Nombre", txtInvNombre));
        form.add(crearInputInv("Categoría", txtInvCategoria));
        form.add(crearInputInv("Precio $", txtInvPrecio));
        form.add(crearInputInv("Stock Disponible", txtInvStock));

        form.add(
                crearControlCantidad(
                        txtInvStock,
                        btnInvStockMenos,
                        btnInvStockMas
                )
        );

        form.add(crearInputInv("Ruta Imagen", txtInvRutaImagen));
        form.add(btnSeleccionarImagen);
        form.add(btnInvAgregar);
        form.add(btnInvModificar);
        form.add(btnInvEliminar);

        pnl.add(centro, BorderLayout.CENTER);
        pnl.add(form, BorderLayout.EAST);

        return pnl;
    }

    private JPanel construirPanelCompras() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(new EmptyBorder(30, 30, 30, 30));

        PanelRedondeado form = new PanelRedondeado(25, COLOR_CARD);
        form.setPreferredSize(new Dimension(430, 0));
        form.setLayout(new GridLayout(10, 1, 0, 12));
        form.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel titulo = new JLabel("ENTRADA DE MERCANCÍA");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));

        form.add(titulo);
        form.add(crearInputInv("Código producto", txtCompraCodigo));
        form.add(crearInputInv("Proveedor", txtCompraProveedor));
        form.add(crearInputInv("Precio compra unitario", txtCompraPrecio));
        form.add(crearInputInv("Cantidad", txtCompraCantidad));

        form.add(
                crearControlCantidad(
                        txtCompraCantidad,
                        btnCompraMenos,
                        btnCompraMas
                )
        );

        form.add(btnRegistrarCompra);

        txtResultadoCompra.setBackground(COLOR_CARD);
        txtResultadoCompra.setForeground(Color.WHITE);
        txtResultadoCompra.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtResultadoCompra.setEditable(false);
        txtResultadoCompra.setText(
                "Aquí aparecerá el resultado de la entrada de mercancía.\n\n" +
                "Ejemplo:\n" +
                "Código: P001\n" +
                "Cantidad: 20\n" +
                "Proveedor: Distribuidora Norte\n"
        );

        JScrollPane scroll = new JScrollPane(txtResultadoCompra);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(30, 41, 59), 2));

        pnl.add(form, BorderLayout.WEST);
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel construirPanelAuditoriaTickets() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controles.setOpaque(false);

        JLabel lbl = new JLabel("Seleccione Folio de Venta: ");
        lbl.setForeground(Color.WHITE);

        cmbTicketsGenerados.setPreferredSize(new Dimension(200, 30));

        controles.add(lbl);
        controles.add(cmbTicketsGenerados);
        controles.add(btnVerTicket);

        txtVisorTicket.setBackground(COLOR_CARD);
        txtVisorTicket.setForeground(Color.WHITE);
        txtVisorTicket.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtVisorTicket.setEditable(false);

        JScrollPane scroll = new JScrollPane(txtVisorTicket);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(30, 41, 59), 2));

        pnl.add(controles, BorderLayout.NORTH);
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel construirPanelReportes() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controles.setOpaque(false);

        JLabel lblFecha = new JLabel("Fecha dd/MM/yyyy: ");
        lblFecha.setForeground(Color.WHITE);

        txtFechaReporte.setPreferredSize(new Dimension(130, 30));
        txtFechaReporte.setText("20/05/2026");

        controles.add(lblFecha);
        controles.add(txtFechaReporte);
        controles.add(btnReporteFecha);
        controles.add(btnExportarReporte);
        controles.add(btnReporteStockBajo);

        JTextArea txtInfo = txtVisorTicket;
        txtInfo.setBackground(COLOR_CARD);
        txtInfo.setForeground(Color.WHITE);
        txtInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtInfo.setEditable(false);

        JScrollPane scroll = new JScrollPane(txtInfo);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(30, 41, 59), 2));

        pnl.add(controles, BorderLayout.NORTH);
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel crearControlCantidad(
            JTextField txt,
            JButton btnMenos,
            JButton btnMas
    ) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(140, 35));

        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setBackground(new Color(30, 41, 59));
        txt.setForeground(Color.WHITE);
        txt.setFont(new Font("SansSerif", Font.BOLD, 14));
        txt.setBorder(
                BorderFactory.createLineBorder(
                        new Color(51, 65, 85)
                )
        );

        btnMenos.setPreferredSize(new Dimension(40, 35));
        btnMas.setPreferredSize(new Dimension(40, 35));

        panel.add(btnMenos, BorderLayout.WEST);
        panel.add(txt, BorderLayout.CENTER);
        panel.add(btnMas, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearInputInv(String titulo, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        txt.setBackground(new Color(30, 41, 59));
        txt.setForeground(Color.WHITE);

        txt.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        titulo, 0, 0, null, COLOR_TEXT_MUTED
                )
        );

        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    public class PanelRedondeado extends JPanel {
        private int r;
        private Color c;

        public PanelRedondeado(int r, Color c) {
            this.r = r;
            this.c = c;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setColor(c);
            g2.fill(
                    new RoundRectangle2D.Double(
                            0, 0, getWidth(), getHeight(), r, r
                    )
            );
            g2.dispose();
        }
    }

    public class BotonPildora extends JButton {
        private Color bg;

        public BotonPildora(String t, Color bg, Color fg) {
            super(t);
            this.bg = bg;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(fg);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void setBackground(Color bg) {
            this.bg = bg;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setColor(bg);
            int h = getHeight();
            g2.fill(
                    new RoundRectangle2D.Double(
                            0, 0, getWidth(), getHeight(), h, h
                    )
            );
            g2.dispose();
            super.paintComponent(g);
        }
    }
}