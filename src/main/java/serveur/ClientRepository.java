package serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import app.ProtocoleTypes;

public class ClientRepository {
    private HashMap<ProtocoleTypes, List<Client>> clients = new HashMap<ProtocoleTypes, List<Client>>();

    public void addClient(ProtocoleTypes protocole, Client client) {
	if (!clients.containsKey(protocole)) {
	    clients.put(protocole, new ArrayList<Client>());
	}
	if (getClient(protocole, client.userId).isPresent()) {
	    throw new RuntimeException("Un utilisateur avec cet id a déjà été enregistré");
	}
	clients.get(protocole).add(client);
    }

    public Optional<Client> getClient(ProtocoleTypes protocole, String clientID) {
	if (!clients.containsKey(protocole)) {
	    return Optional.empty();
	}
	return clients.get(protocole).stream().filter(client -> client.userId.equals(clientID)).findFirst();
    }

    public List<Client> getAllClients(ProtocoleTypes protocole) {

	return clients.getOrDefault(protocole, new ArrayList<Client>());

    }

}
