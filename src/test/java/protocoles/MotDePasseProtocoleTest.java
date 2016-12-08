package protocoles;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import app.Crypto;
import app.EtapeProtocole;
import app.ProtocoleTypes;
import client.ClientController;
import pirate.PirateController;
import serveur.Client;
import serveur.ServeurController;

public class MotDePasseProtocoleTest {

    private static final String USER_ID = "thefuture2092";
    private static final String NOM = "Aristote";
    private static final String PASSKEY = "blablaCar";
    private static final String HASHED_PASSWORD = "has-snf-667-54822";
    private static final String MESSAGE_ENREGISTREMENT = String.format("%s %s %s", USER_ID, NOM, PASSKEY);
    private static final String SUCCESS = "200";
    private static final String SESSION_ID = "12345";
    private static final String MESSAGE_DEMANDE_AUTHENTICATION = String.format("%s %s %s", SESSION_ID, USER_ID,
	    PASSKEY);
    private static final String ACCESS_DENIED = "401";
    private static final String NS = "341";
    private static final String MESSAGE_SUCCESS_AUTHENTICATION = String.format("%s 200 SetCookie:Session=%s",
	    SESSION_ID, NS);
    private ClientController client;
    private ServeurController serveur;
    private MotDePasseProtocole protocole;
    private Crypto crypto;
    private PirateController pirate;

    @Before
    public void setUp() {
	client = Mockito.mock(ClientController.class);
	serveur = Mockito.mock(ServeurController.class);
	pirate = Mockito.mock(PirateController.class);
	crypto = Mockito.mock(Crypto.class);

	Mockito.when(crypto.h1(PASSKEY)).thenReturn(HASHED_PASSWORD);
	Mockito.when(crypto.random5Digits()).thenReturn(SESSION_ID);

	protocole = new MotDePasseProtocole(serveur, client, pirate, crypto);
    }

    @Test
    public void quandExecuteEtapeE1MessageEnregistrementEstIntercepteParPirate() {
	protocole.executeEtape(EtapeProtocole.E1, MESSAGE_ENREGISTREMENT);
	Mockito.verify(pirate).intercepteMessage(EtapeProtocole.E1, MESSAGE_ENREGISTREMENT);
    }

    @Test
    public void givenMessageWhenCompleteE1ItShouldSaveUserWithHashedPassword() {
	protocole.completeEtape(EtapeProtocole.E1, MESSAGE_ENREGISTREMENT);
	Mockito.verify(serveur).enregistreUtilisateur(USER_ID, NOM, HASHED_PASSWORD, ProtocoleTypes.MOT_DE_PASSE);
    }

    @Test
    public void givenMessageWhenExecuteE2ItShouldSendMessageToPirate() {
	protocole.executeEtape(EtapeProtocole.E2, SUCCESS);
	Mockito.verify(pirate).intercepteMessage(EtapeProtocole.E2, SUCCESS);
    }

    @Test
    public void quandExecuteEtapeA1DemandeAuthenticationEstIntercepteParPirate() {
	protocole.executeEtape(EtapeProtocole.A1, USER_ID + " " + PASSKEY);
	Mockito.verify(pirate).intercepteMessage(EtapeProtocole.A1, SESSION_ID + " " + USER_ID + " " + PASSKEY);
    }

    @Test
    public void givenCorrectAuthenticationWhenCompleteA1ServeurShouldReceiveMessageSuccessAuthentication() {
	givenConditionsAuthenticationRemplies();
	protocole.completeEtape(EtapeProtocole.A1, SESSION_ID + " " + USER_ID + " " + PASSKEY);
	Mockito.verify(serveur).recoisMessage(MESSAGE_SUCCESS_AUTHENTICATION);
    }

    private void givenConditionsAuthenticationRemplies() {
	givenClientAvecIDetPasswordExiste();
	givenCryptoReturnNS();
    }

    private void givenCryptoReturnNS() {
	Mockito.when(crypto.random5Digits()).thenReturn(NS);
    }

    private void givenClientAvecIDetPasswordExiste() {
	Mockito.when(serveur.reqClientParID(protocole.reqProtocoleType(), USER_ID))
		.thenReturn(Optional.of(new Client(USER_ID, NOM, HASHED_PASSWORD)));
    }

}
