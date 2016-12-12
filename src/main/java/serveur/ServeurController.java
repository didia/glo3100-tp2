package serveur;

import java.util.List;
import java.util.Optional;

import app.EtapeProtocole;
import app.Protocole;
import app.ProtocoleTypes;
import app.User;

public class ServeurController {

    private ServeurRepository repository;
    private Protocole protocole;
    private ServeurUI ui;
    private String dernierMessageRecu = null;

    public ServeurController(ServeurRepository repository) {
	this.repository = repository;
    }

    public void recoisMessage(String message) {
	this.dernierMessageRecu = message;
    }

    public void enregistreUtilisateur(String userID, String nom, String passkey, ProtocoleTypes typeProtocole) {
	User user = new User(nom, userID, passkey);
	this.repository.addClient(typeProtocole, user);
    }

    public void afficheClients() {
	List<User> users = this.repository.getAllClients(protocole.reqProtocoleType());
	if (ui != null) {
	    ui.afficheClients(users);
	}
    }

    public void setProtocole(Protocole protocole) {
	this.protocole = protocole;
    }

    public void setUI(ServeurUI ui) {
	this.ui = ui;
    }

    public void executeE2() {
	envoyerDernierMessageRecu(EtapeProtocole.E2);
    }

    public void executeA2() {
	envoyerDernierMessageRecu(EtapeProtocole.A2);
    }

    public void executeA4() {
	envoyerDernierMessageRecu(EtapeProtocole.A4);
    }

    public void executeT2() {
	envoyerDernierMessageRecu(EtapeProtocole.T2);
    }

    public void executeT4() {
	envoyerDernierMessageRecu(EtapeProtocole.T4);
    }

    private void envoyerDernierMessageRecu(EtapeProtocole etape) {
	if (dernierMessageRecu != null) {
	    protocole.executeEtape(etape, dernierMessageRecu);
	} else {
	    System.out.println("Dernier message réçu est null");
	}
    }

    public Optional<User> reqClientParID(ProtocoleTypes protocole, String userId) {
	return repository.getClient(protocole, userId);
    }

}
