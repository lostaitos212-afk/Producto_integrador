package main.controller;

import main.view.LoginView;
import main.view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class LoginController implements ActionListener {

    private LoginView vistaLogin;

    public LoginController(LoginView vistaLogin) {

        this.vistaLogin = vistaLogin;

        if (this.vistaLogin.btnEntrar != null) {

            this.vistaLogin.btnEntrar.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaLogin.btnEntrar) {

            String user =
                    vistaLogin.txtUsuario
                            .getText();

            String pass =
                    new String(
                            vistaLogin.txtPassword
                                    .getPassword()
                    );

            if (user.equals("admin")
                    && pass.equals("1234")) {

                vistaLogin.dispose();

                MainView vistaPrincipal =
                        new MainView();

                new VentaController(vistaPrincipal);
                new InventarioController(vistaPrincipal);
                new CompraController(vistaPrincipal);

                vistaPrincipal.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                        vistaLogin,
                        "Credenciales incorrectas.\nUsuario: admin\nClave: 1234",
                        "Error de Acceso",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}