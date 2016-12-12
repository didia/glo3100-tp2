package protocoles;

import java.util.HashMap;

import app.Crypto;
import app.EtapeProtocole;
import app.Protocole;
import app.ProtocoleTypes;
import client.ClientController;
import pirate.PirateController;
import serveur.ServeurController;

public abstract class BaseProtocole implements Protocole {

    protected ClientController client;
    protected ServeurController serveur;
    protected Crypto crypto;
    protected PirateController pirate;

    protected HashMap<String, String> sessionMap = new HashMap<String, String>();

    public BaseProtocole(ServeurController serveur, ClientController client, PirateController pirate, Crypto crypto) {
	this.client = client;
	this.serveur = serveur;
	this.crypto = crypto;
	this.pirate = pirate;
    }

    protected String getNxHash(String sessionID, String ns, String password) {
	return crypto.h1(String.format("%s.%s.%s", sessionID, ns, crypto.h2(password)));
    }

    @Override
    public abstract void executeEtape(EtapeProtocole etape, String message);

    @Override
    public abstract void completeEtape(EtapeProtocole etape, String message);

    @Override
    public abstract ProtocoleTypes reqProtocoleType();

}
