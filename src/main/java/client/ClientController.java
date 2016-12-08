package client;

import java.util.HashMap;

import app.EtapeProtocole;
import protocoles.CurrentProtocole;

public class ClientController {

    private CurrentProtocole protocole;
    private HashMap<String, String> cookies = new HashMap<String, String>();

    public void executeE1(String userID, String nom, String password) {
	protocole.executeEtape(EtapeProtocole.E1, String.format("%s %s %s", userID, nom, password));
    }

    public void setProtocole(CurrentProtocole protocole) {
	this.protocole = protocole;
    }

    public void executeA1(String userID, String pwd) {
	protocole.executeEtape(EtapeProtocole.A1, String.format("%s %s", userID, pwd));
    }

    public void executeA3() {

    }

    public void executeT1(String transaction) {
	protocole.executeEtape(EtapeProtocole.T1, transaction);
    }

    public void executeT3() {

    }

    public void changerProtocole() {

    }

    public void afficherTrousseCles() {

    }

    public void setCookie(String key, String value) {
	cookies.put(key, value);
    }

    public String getCookie(String key) {
	return cookies.get(key);
    }

    public void recoisMessage(String message) {

    }

}
