package edu.virginia.sde.reviews;
import java.sql.SQLException;
public class UserInferface {
    private final DatabaseDriver databaseDriver;

    public UserInferface(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public User getUser(String username, String password) {
        try {
            databaseDriver.connect();
            var user = databaseDriver.getUser(username);
            databaseDriver.disconnect();
            verifyUserInfo(user, password);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(User user) {
        try {
            databaseDriver.connect();
            databaseDriver.addUser(user);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUsernameAvailable(String username) {
        try {
            databaseDriver.connect();
            boolean isAvailable = databaseDriver.isUserInDatabase(username);
            databaseDriver.disconnect();
            return !isAvailable; // Assuming isUserInDatabase returns true if user exists
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyUserInfo(User user, String password) {
        if (user == null) {
            throw new InvalidUsernameException();
        }
        if (!password.equals(user.getPassword())) {
            throw new IncorrectPassException();
        }
    }

    // Excep
}


