package protocoles;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import app.Crypto;
import app.EtapeProtocole;
import app.ProtocoleTypes;
import app.User;
import client.ClientController;
import pirate.PirateController;
import serveur.ServeurController;

public class UAFProtocole extends BaseProtocole {

    private User currentClient = null;

    public UAFProtocole(ServeurController serveur, ClientController client, PirateController pirate, Crypto crypto) {
	super(serveur, client, pirate, crypto);
    }

    @Override
    public void executeEtape(EtapeProtocole etape, String message) {
	String[] messageParts = message.split(" ");
	switch (etape) {
	case A1:
	    message = genereSessionID(messageParts);
	    break;
	case A3:
	    message = generateNsHash(message);
	    break;
	case A4:
	    break;
	case E1:
	    message = enregistreUtilisateurClient(messageParts);
	    break;
	case T1:
	    message = getTransactionMessage(message);
	    break;
	case T2:
	    break;
	case T3:
	    message = generateNsCommandeSigne(message);
	    break;
	case T4:
	    break;
	default:
	    break;
	}
	pirate.intercepteMessage(etape, message);
    }

    private String generateNsCommandeSigne(String masterPassword) {
	if (masterPassword.isEmpty()) {
	    throw new ProtocolError("Veuillez entre le mot de passe");
	}

	Optional<User> user = client.reqUserParID(client.getCookie("USER_ID"), masterPassword);
	if (!user.isPresent()) {
	    throw new ProtocolError("Le mot de passe entré est invalide");
	}

	try {
	    String nsComandeSigne = crypto.signeH1RSA(client.getCookie("COMMANDE") + "." + client.getCookie("NS"),
		    user.get().passKey);
	    return String.format("%s %s", client.getCookie("SESSION_ID"), nsComandeSigne);
	} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
	    throw new ProtocolError(e);
	}

    }

    private String getTransactionMessage(String transaction) {
	if (transaction.isEmpty()) {
	    throw new ProtocolError("Le champ transaction doit être rempli ");
	}
	return String.format("%s %s", client.getCookie("SESSION_ID"), transaction);
    }

    private String generateNsHash(String password) {
	try {
	    String sessionID = client.getCookie("SESSION_ID");
	    String nsHashSigne = crypto.signeH1RSA(client.getCookie("NS"), client.getCookie("PRIVATE_KEY"));
	    return String.format("%s %s", sessionID, nsHashSigne);
	} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
	    throw new ProtocolError(e);
	}
    }

    private String genereSessionID(String[] messageParts) {
	if (messageParts.length == 0) {
	    throw new ProtocolError("Le userId et le master password sont réquis pour l'authentication");
	}
	Optional<User> user = client.reqUserParID(messageParts[0], messageParts[1]);
	if (!user.isPresent()) {
	    throw new ProtocolError("L'utilisateur avec cet userID n'a pas été enregistré");
	}
	client.setCookie("USER_ID", messageParts[0]);
	client.setCookie("PRIVATE_KEY", user.get().passKey);
	return String.format("%s %s", crypto.random5Digits(), messageParts[0]);
    }

    private String enregistreUtilisateurClient(String[] messageParts) {
	if (messageParts.length != 3) {
	    throw new ProtocolError(
		    String.format("Les champs nom, userID et password doivent être renseignés", messageParts.length));
	}

	try {
	    KeyPair keyPair = crypto.generateKey();
	    String privateKey = crypto.encodeBase64(keyPair.getPrivate().getEncoded());
	    String publicKey = crypto.encodeBase64(keyPair.getPublic().getEncoded());
	    client.enregistreUtilisateur(messageParts[0], messageParts[1], privateKey, messageParts[2]);
	    return String.format("%s %s %s", messageParts[0], messageParts[1], publicKey);
	} catch (NoSuchAlgorithmException e) {
	    throw new ProtocolError("Impossible de générer une paire des clés RSA: " + e.getMessage());
	}
    }

    @Override
    public void completeEtape(EtapeProtocole etape, String message) {
	String[] messageParts = message.split(" ");
	switch (etape) {
	case A1:
	    message = genereNSPourSession(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case A2:
	    enregistrerSessionIDEtNS(messageParts);
	    break;
	case A3:
	    message = checkNsHash(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case A4:
	    break;
	case E1:
	    message = enregistreUtilisateurServeur(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case T1:
	    message = verifieSessionID(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case T2:
	    enregistreNSPrime(messageParts);
	    break;
	case T3:
	    message = checkNsHash(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case T4:
	    break;
	default:
	    break;

	}

    }

    private void enregistreNSPrime(String[] messageParts) {
	if (messageParts[0].equals("401")) {
	    throw new ProtocolError("La Session ID n'est pas autorisé");
	}
	if (messageParts.length != 3) {
	    throw new ProtocolError("Le format du message réçu est incorrect");
	}

	client.setCookie("COMMANDE", messageParts[1]);
	client.setCookie("NS", messageParts[2]);
    }

    private String verifieSessionID(String[] messageParts) {
	if (sessionMap.containsKey(messageParts[0])) {
	    String nsPrime = crypto.random5Digits();
	    sessionMap.put(messageParts[0], messageParts[1] + "." + nsPrime);
	    return String.format("%s %s %s", messageParts[0], messageParts[1], nsPrime);
	} else {
	    return "401";
	}
    }

    private void enregistrerSessionIDEtNS(String[] messageParts) {
	if (messageParts.length != 2) {
	    throw new ProtocolError(String.format("Le message devrait contenir l'ID et le NS", messageParts.length));
	}
	client.setCookie("SESSION_ID", messageParts[0]);
	client.setCookie("NS", messageParts[1]);
    }

    private String checkNsHash(String[] messageParts) {
	String publicKey = currentClient.passKey;
	String nsHashSigne = messageParts[1];
	String ns = sessionMap.get(messageParts[0]);
	String code;
	try {
	    if (crypto.verifyH1RSA(ns, publicKey, nsHashSigne)) {
		code = "200";
	    } else {
		code = "401";
	    }
	    return String.format("%s %s", messageParts[0], code);
	} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
	    throw new ProtocolError(e);
	}
    }

    private String genereNSPourSession(String[] messageParts) {
	Optional<User> user = serveur.reqClientParID(reqProtocoleType(), messageParts[1]);
	if (!user.isPresent()) {
	    return "401";
	}
	currentClient = user.get();
	sessionMap.put(messageParts[0], crypto.random5Digits());
	return String.format("%s %s", messageParts[0], sessionMap.get(messageParts[0]));
    }

    private String enregistreUtilisateurServeur(String[] messageParts) {
	if (messageParts.length != 3) {
	    throw new ProtocolError(String.format(
		    "Le message attendu à l'étape d'enregistrement devrait avoir 3 parties, seulement %ds réçu",
		    messageParts.length));
	}
	serveur.enregistreUtilisateur(messageParts[0], messageParts[1], messageParts[2], reqProtocoleType());
	return "200";
    }

    @Override
    public ProtocoleTypes reqProtocoleType() {
	return ProtocoleTypes.UAF;
    }

}
