package userRest.connection;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import userRest.model.User;

public class UserDAO {

    // Obtener todos los usuarios
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, name, email, password FROM USUARIOS";

        // Log para saber que se está ejecutando la consulta
        System.out.println("Iniciando consulta para obtener todos los usuarios: " + query);

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("Consulta ejecutada: " + query); // Agregar log aquí
            
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")));
                
            }
            // Verificar si la lista está vacía
            if (users.isEmpty()) {
                System.out.println("No se encontraron usuarios en la base de datos.");
            } else {
                System.out.println("Usuarios obtenidos: " + users.size());
            }
            System.out.println("Usuarios obtenidos: " + users.size()); // Log para verificar cuántos usuarios obtuvimos

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage()); // Log para mostrar el error
            e.printStackTrace();
        }
        return users;
    }

    // Obtener usuario por ID
    public static User getUserById(long id) {
        String query = "SELECT id, name, email, password FROM USUARIOS WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createUser(String name, String email, String password) {
        String query = "INSERT INTO USUARIOS (name, email, password) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit(); // Confirmar transacción
                    return true;
                } else {
                    conn.rollback(); // Revertir en caso de error
                    return false;
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar configuración de autocommit
                    conn.close(); // Cerrar la conexión
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Actualizar usuario
    public static boolean updateUser(long id, String name, String email, String password) {
        String query = "UPDATE USUARIOS SET name = ?, email = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setLong(4, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar usuario
    public static boolean deleteUser(long id) {
        String query = "DELETE FROM USUARIOS WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
