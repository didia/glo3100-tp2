package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.User;

public class ClientRepository {
    private List<User> users = new ArrayList<User>();

    public void addClient(User user, String masterPassword) {
	users.add(user);
    }

    public Optional<User> getClient(String clientID, String masterPassword) {

	return users.stream().filter(client -> client.userId.equals(clientID)).findFirst();
    }

    public List<User> getAllClients(String masterPassword) {

	return users;

    }

}
