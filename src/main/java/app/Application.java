package app;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;

import client.ClientController;
import client.ClientRepository;
import client.ClientUI;
import pirate.PirateController;
import pirate.PirateUI;
import protocoles.CurrentProtocole;
import serveur.ServeurController;
import serveur.ServeurRepository;
import serveur.ServeurUI;

public class Application {

    private JFrame frame;
    private ClientUI client;
    private PirateUI pirate;
    private ServeurUI serveur;
    private final ProtocoleTypes DEFAULT_PROTOCOL_TYPE = ProtocoleTypes.MOT_DE_PASSE;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    Application window = new Application();
		    window.frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the application.
     */
    public Application() {
	ServeurRepository serveurRepository = new ServeurRepository();
	ClientRepository clientRepository = new ClientRepository();

	ServeurController serveurController = new ServeurController(serveurRepository);
	ClientController clientController = new ClientController(clientRepository);
	PirateController pirateController = new PirateController();
	CurrentProtocole protocole = new CurrentProtocole(serveurController, clientController, pirateController,
		DEFAULT_PROTOCOL_TYPE);
	serveurController.setProtocole(protocole);
	clientController.setProtocole(protocole);

	client = new ClientUI(clientController);
	serveur = new ServeurUI(serveurController);
	pirate = new PirateUI(pirateController);
	serveurController.setUI(serveur);
	pirateController.setUI(pirate);

	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	frame = new JFrame();
	frame.setBounds(100, 100, 700, 850);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	GridBagLayout gridBagLayout = new GridBagLayout();
	gridBagLayout.columnWidths = new int[] { 700 };
	gridBagLayout.rowHeights = new int[] { 210, 110, 350, 0 };
	gridBagLayout.columnWeights = new double[] { 0.0 };
	gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
	frame.getContentPane().setLayout(gridBagLayout);

	GridBagConstraints gbc_serveur = new GridBagConstraints();
	gbc_serveur.fill = GridBagConstraints.BOTH;
	gbc_serveur.insets = new Insets(0, 0, 5, 0);
	gbc_serveur.gridx = 0;
	gbc_serveur.gridy = 0;
	frame.getContentPane().add(serveur, gbc_serveur);
	GridBagConstraints gbc_pirate = new GridBagConstraints();
	gbc_pirate.fill = GridBagConstraints.BOTH;
	gbc_pirate.insets = new Insets(0, 0, 5, 0);
	gbc_pirate.gridx = 0;
	gbc_pirate.gridy = 1;
	frame.getContentPane().add(pirate, gbc_pirate);

	GridBagConstraints gbc_client = new GridBagConstraints();
	gbc_client.fill = GridBagConstraints.BOTH;
	gbc_client.gridx = 0;
	gbc_client.gridy = 2;
	frame.getContentPane().add(client, gbc_client);
    }

}
