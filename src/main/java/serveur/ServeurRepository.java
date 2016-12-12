package serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import app.User;
import app.ProtocoleTypes;

public class ServeurRepository {
    private HashMap<ProtocoleTypes, List<User>> users = new HashMap<ProtocoleTypes, List<User>>();

    public void addClient(ProtocoleTypes protocole, User user) {
	if (!users.containsKey(protocole)) {
	    users.put(protocole, new ArrayList<User>());
	}
	if (getClient(protocole, user.userId).isPresent()) {
	    throw new RuntimeException("Un utilisateur avec cet id a déjà été enregistré");
	}
	users.get(protocole).add(user);
    }

    public Optional<User> getClient(ProtocoleTypes protocole, String clientID) {
	if (!users.containsKey(protocole)) {
	    return Optional.empty();
	}
	return users.get(protocole).stream().filter(client -> client.userId.equals(clientID)).findFirst();
    }

    public List<User> getAllClients(ProtocoleTypes protocole) {

	return users.getOrDefault(protocole, new ArrayList<User>());

    }

}
