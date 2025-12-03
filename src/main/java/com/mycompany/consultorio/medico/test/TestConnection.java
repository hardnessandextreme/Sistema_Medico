package com.mycompany.consultorio.medico.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/sistema_medico";
        String user = "postgres";
        String password = "postgres";
        
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver cargado correctamente");
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa!");
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            
            if (rs.next()) {
                System.out.println("Usuarios en la base de datos: " + rs.getInt(1));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
