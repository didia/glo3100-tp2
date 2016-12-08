package serveur;

import java.util.List;
import java.util.Optional;

import app.EtapeProtocole;
import app.Protocole;
import app.ProtocoleTypes;

public class ServeurController {

    private ClientRepository repository;
    private Protocole protocole;
    private ServeurUI ui;
    private String dernierMessageRecu = null;

    public ServeurController(ClientRepository repository) {
	this.repository = repository;
    }

    public void recoisMessage(String message) {
	this.dernierMessageRecu = message;
    }

    public void enregistreUtilisateur(String userID, String nom, String passkey, ProtocoleTypes typeProtocole) {
	Client client = new Client(nom, userID, passkey);
	this.repository.addClient(typeProtocole, client);
    }

    public void afficheClients() {
	List<Client> clients = this.repository.getAllClients(protocole.reqProtocoleType());
	if (ui != null) {
	    ui.afficheClients(clients);
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

    public void executeT2() {
	envoyerDernierMessageRecu(EtapeProtocole.T2);
    }

    private void envoyerDernierMessageRecu(EtapeProtocole etape) {
	if (dernierMessageRecu != null) {
	    protocole.executeEtape(etape, dernierMessageRecu);
	} else {
	    System.out.println("Dernier message réçu est null");
	}
    }

    public Optional<Client> reqClientParID(ProtocoleTypes protocole, String userId) {
	return repository.getClient(protocole, userId);
    }

}
