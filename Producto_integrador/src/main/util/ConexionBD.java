package main.util;



import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;



public class ConexionBD {



    private static final String URL =

        "jdbc:mysql://localhost:3306/mega_pos_db";



    private static final String USER = "root";



    private static final String PASS = "123";



    public static Connection getConexion() {



        try {



            return DriverManager.getConnection(

                    URL,

                    USER,

                    PASS

            );



        } catch (SQLException e) {



            ManejadorErrores.registrarError(

                    "Error de conexión a la Base de Datos",

                    e

            );



            return null;

        }

    }

} 