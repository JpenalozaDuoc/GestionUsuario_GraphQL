package userRest.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import userRest.connection.UserDAO;

public class UserMutationResolver implements GraphQLMutationResolver{

     // Mutación: Crear usuario
    public boolean createUser(String name, String email, String password) {
        return UserDAO.createUser(name, email, password);
    }

    // Mutación: Actualizar usuario
    public boolean updateUser(Long id, String name, String email, String password) {
        return UserDAO.updateUser(id, name, email, password);
    }

    // Mutación: Eliminar usuario
    public boolean deleteUser(Long id) {
        return UserDAO.deleteUser(id);
    }
}
