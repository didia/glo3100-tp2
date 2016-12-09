package protocoles;

import app.Crypto;
import app.EtapeProtocole;
import app.Protocole;
import app.ProtocoleTypes;
import client.ClientController;
import pirate.PirateController;
import serveur.ServeurController;

public class CurrentProtocole implements Protocole {
    private Protocole currentProtocole;
    private ServeurController serveurController;
    private ClientController clientController;
    private Crypto crypto;
    private PirateController pirateController;

    public CurrentProtocole(ServeurController serveur, ClientController client, PirateController pirate,
	    ProtocoleTypes protocoleType) {
	this.serveurController = serveur;
	this.clientController = client;
	this.pirateController = pirate;

	this.serveurController.setProtocole(this);
	this.clientController.setProtocole(this);
	this.pirateController.setProtocole(this);
	this.crypto = new Crypto();
	setProtocoleTypes(protocoleType);
    }

    @Override
    public void executeEtape(EtapeProtocole etape, String message) {
	currentProtocole.executeEtape(etape, message);
    }

    @Override
    public ProtocoleTypes reqProtocoleType() {
	return currentProtocole.reqProtocoleType();
    }

    public void setProtocoleTypes(ProtocoleTypes protocoleType) {
	if (currentProtocole != null && currentProtocole.reqProtocoleType().equals(protocoleType)) {
	    return;
	}
	switch (protocoleType) {
	case MOT_DE_PASSE:
	    currentProtocole = new MotDePasseProtocole(serveurController, clientController, pirateController, crypto);
	    break;
	case NTLM_V2:
	    currentProtocole = new NTLMProtocole(serveurController, clientController, pirateController, crypto);
	    break;
	case UAF:
	    break;
	default:
	    break;

	}
    }

    public void completeEtape(EtapeProtocole etape, String message) {
	currentProtocole.completeEtape(etape, message);
    }

}
