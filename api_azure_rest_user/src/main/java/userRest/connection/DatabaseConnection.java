package userRest.connection;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());


    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Pase por Base de datos 1");
            boolean isValid = conn.isValid(5);
            logger.info("Conexión a la base de datos probada: " + (isValid ? "Válida" : "Inválida"));
            return isValid;
            
        } catch (SQLException e) {
            logger.severe("Error al probar la conexión: " + e.getMessage());
            return false;
        }
    }

    public static Connection getConnection() throws SQLException {
    
        String url = "jdbc:oracle:thin:@" + System.getenv("DB_TNS_ADMIN") + "?TNS_ADMIN=" + System.getenv("DB_WALLET_PATH");
        String user = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String walletLocation = System.getenv("DB_WALLET_PATH");
        System.out.println("Pase por Base de datos 2"+url+"/"+user+"()"+walletLocation);
        // Configurar propiedades de la conexión
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("oracle.net.ssl_version", "1.2");
        props.setProperty("oracle.net.wallet_location", "(SOURCE=(METHOD=file)(METHOD_DATA=(DIRECTORY=" + walletLocation + ")))");

        // Registrar información de la conexión
        logger.info("Configurando conexión a la base de datos:");
        logger.info("URL: " + url);
        logger.info("Usuario: " + user);
        logger.info("Ubicación del wallet: " + props.getProperty("oracle.net.wallet_location"));

        try {
            // Establecer la conexión
            Connection conn = DriverManager.getConnection(url, props);
            logger.info("Conexión establecida correctamente.");
            return conn;
        } catch (SQLException e) {
            logger.severe("Error al establecer la conexión: " + e.getMessage());
            throw e;
        }
    }
    
}
