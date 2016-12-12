package client;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import app.EtapeProtocole;
import app.ProtocoleTypes;
import app.User;
import protocoles.CurrentProtocole;

public class ClientController {

    private CurrentProtocole protocole;
    private HashMap<String, String> cookies = new HashMap<String, String>();
    private ClientRepository repository;

    public ClientController(ClientRepository repository) {
	this.repository = repository;
    }

    public void executeE1(String userID, String nom, String password) {
	protocole.executeEtape(EtapeProtocole.E1, String.format("%s %s %s", userID, nom, password));
    }

    public void setProtocole(CurrentProtocole protocole) {
	this.protocole = protocole;
    }

    public void executeA1(String userID, String pwd) {
	protocole.executeEtape(EtapeProtocole.A1, String.format("%s %s", userID, pwd));
    }

    public void executeA3(String pwd) {
	protocole.executeEtape(EtapeProtocole.A3, pwd);
    }

    public void executeT1(String transaction) {
	protocole.executeEtape(EtapeProtocole.T1, transaction);
    }

    public void executeT3(String masterPassword) {
	protocole.executeEtape(EtapeProtocole.T3, masterPassword);
    }

    public void changeProtocole(ProtocoleTypes protocoleType) {
	protocole.setProtocoleTypes(protocoleType);
    }

    public List<User> getTrousseCles(String masterPassword) {
	return this.repository.getAllClients(masterPassword);
    }

    public void setCookie(String key, String value) {
	cookies.put(key, value);
    }

    public String getCookie(String key) {
	return cookies.get(key);
    }

    public void recoisMessage(String message) {

    }

    public void supprimeToutCookies() {
	cookies.clear();
    }

    public void enregistreUtilisateur(String userID, String nom, String privateKey, String masterPassword) {
	User user = new User(nom, userID, privateKey);
	this.repository.addClient(user, masterPassword);
    }

    public Optional<User> reqUserParID(String clientID, String masterPassword) {
	return this.repository.getClient(clientID, masterPassword);
    }
}
