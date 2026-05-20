package main.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginView extends JFrame {
    public JTextField txtUsuario = new JTextField();
    public JPasswordField txtPassword = new JPasswordField();
    public JButton btnEntrar;

    private final Color COLOR_BG = new Color(8, 10, 18);
    private final Color COLOR_CARD = new Color(15, 23, 42);
    private final Color COLOR_EMERALD = new Color(16, 185, 129);

    public LoginView() {
        setTitle("MEGAPOS - Acceso Seguro");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new GridBagLayout());

        // Tarjeta Central Oscura
        PanelRedondeado card = new PanelRedondeado(30, COLOR_CARD);
        card.setPreferredSize(new Dimension(320, 360));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 30, 40, 30));

        JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos de texto
        txtUsuario.setMaximumSize(new Dimension(250, 35));
        txtUsuario.setHorizontalAlignment(JTextField.CENTER);
        txtUsuario.setBackground(new Color(30, 41, 59));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)), "Usuario", 0, 0, new Font("SansSerif", Font.PLAIN, 10), Color.LIGHT_GRAY));

        txtPassword.setMaximumSize(new Dimension(250, 35));
        txtPassword.setHorizontalAlignment(JTextField.CENTER);
        txtPassword.setBackground(new Color(30, 41, 59));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)), "Contraseña", 0, 0, new Font("SansSerif", Font.PLAIN, 10), Color.LIGHT_GRAY));

        // Botón de entrar
        btnEntrar = new BotonPildora("ENTRAR AL SISTEMA", COLOR_EMERALD, Color.WHITE);
        btnEntrar.setMaximumSize(new Dimension(250, 40));
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(txtUsuario);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(btnEntrar);

        add(card);
    }

    // Clases de dibujo interno para el Login
    class PanelRedondeado extends JPanel {
        private int r; private Color c;
        public PanelRedondeado(int r, Color c) { this.r=r; this.c=c; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),r,r));
            g2.dispose(); super.paintComponent(g);
        }
    }

    class BotonPildora extends JButton {
        private Color bg;
        public BotonPildora(String t, Color bg, Color fg) {
            super(t); this.bg=bg; setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setForeground(fg); setFont(new Font("SansSerif", Font.BOLD, 12)); setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg); int h = getHeight();
            g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),h,h,h));
            g2.dispose(); super.paintComponent(g);
        }
    }
}