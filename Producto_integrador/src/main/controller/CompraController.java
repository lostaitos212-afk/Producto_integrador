package main.controller;

import main.dao.CompraDAO;
import main.view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class CompraController implements ActionListener {

    private MainView vista;

    private CompraDAO compraDAO =
            new CompraDAO();

    public CompraController(MainView vista) {

        this.vista = vista;

        if (vista.btnRegistrarCompra != null)
            vista.btnRegistrarCompra.addActionListener(this);

        if (vista.btnCompraMenos != null)
            vista.btnCompraMenos.addActionListener(this);

        if (vista.btnCompraMas != null)
            vista.btnCompraMas.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnCompraMenos) {

            cambiarCantidadCompra(-1);
        }

        else if (e.getSource() == vista.btnCompraMas) {

            cambiarCantidadCompra(1);
        }

        else if (e.getSource() == vista.btnRegistrarCompra) {

            registrarCompra();
        }
    }

    private void cambiarCantidadCompra(int cambio) {

        try {

            String texto =
                    vista.txtCompraCantidad
                            .getText()
                            .trim();

            int cantidad;

            if (texto.isEmpty()) {

                cantidad = 1;

            } else {

                cantidad =
                        Integer.parseInt(texto);
            }

            cantidad += cambio;

            if (cantidad < 1) {

                cantidad = 1;
            }

            vista.txtCompraCantidad.setText(
                    String.valueOf(cantidad)
            );

        } catch (NumberFormatException e) {

            vista.txtCompraCantidad.setText("1");
        }
    }

    private void registrarCompra() {

        try {

            String codigo =
                    vista.txtCompraCodigo
                            .getText()
                            .trim();

            String proveedor =
                    vista.txtCompraProveedor
                            .getText()
                            .trim();

            String cantidadTexto =
                    vista.txtCompraCantidad
                            .getText()
                            .trim();

            String precioTexto =
                    vista.txtCompraPrecio
                            .getText()
                            .trim();

            if (codigo.isEmpty()
                    || proveedor.isEmpty()
                    || cantidadTexto.isEmpty()
                    || precioTexto.isEmpty()) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Complete todos los campos de compra."
                );

                return;
            }

            int cantidad =
                    Integer.parseInt(cantidadTexto);

            double precioCompra =
                    Double.parseDouble(precioTexto);

            if (cantidad <= 0 || precioCompra < 0) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Cantidad y precio deben ser válidos."
                );

                return;
            }

            boolean ok =
                    compraDAO.registrarEntradaMercancia(
                            codigo,
                            cantidad,
                            precioCompra,
                            proveedor
                    );

            if (ok) {

                String mensaje =
                        "ENTRADA REGISTRADA CORRECTAMENTE\n\n"
                                + "Código producto: "
                                + codigo
                                + "\nProveedor: "
                                + proveedor
                                + "\nCantidad agregada: "
                                + cantidad
                                + "\nPrecio compra unitario: $"
                                + precioCompra
                                + "\nTotal compra: $"
                                + (cantidad * precioCompra);

                vista.txtResultadoCompra.setText(mensaje);

                JOptionPane.showMessageDialog(
                        vista,
                        "Entrada de mercancía registrada."
                );

                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo registrar la entrada.\nVerifique que el producto exista."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Cantidad y precio deben ser numéricos."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al registrar compra."
            );

            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {

        vista.txtCompraCodigo.setText("");
        vista.txtCompraProveedor.setText("");
        vista.txtCompraCantidad.setText("1");
        vista.txtCompraPrecio.setText("");
    }
}