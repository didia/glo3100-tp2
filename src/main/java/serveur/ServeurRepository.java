package serveur;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.User;
import app.ProtocoleTypes;

public class ServeurRepository {
	private String filename;
	private Gson gson = new Gson();
    private HashMap<ProtocoleTypes, List<User>> users = new HashMap<ProtocoleTypes, List<User>>();

    public ServeurRepository(String filename) {
		this.filename = filename;
		loadRepositoryContent();
	}
    
    private void loadRepositoryContent() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			users = (HashMap<ProtocoleTypes, List<User>>) gson.fromJson(br, users.getClass());
		} catch (FileNotFoundException e) {
			System.out.println("Loading repository content failed.");
			System.out.println("Reason: " + e.getMessage());
		}
	}

	public void addClient(ProtocoleTypes protocole, User user) {
		if (!users.containsKey(protocole)) {
		    users.put(protocole, new ArrayList<User>());
		}
		if (getClient(protocole, user.userId).isPresent()) {
		    throw new RuntimeException("Un utilisateur avec cet id a déjà été enregistré");
		}
		users.get(protocole).add(user);
		saveRepositoryContent();
    }
	
	private void saveRepositoryContent() {
		try (Writer writer = new FileWriter(filename)) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(users, writer);
		} catch (IOException e) {
			System.out.println("Saving repository content failed.");
			System.out.println("Reason: " + e.getMessage());
		}
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
