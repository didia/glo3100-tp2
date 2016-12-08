package serveur;

public class Client {
    public final String name;
    public final String userId;
    public final String passKey;

    public Client(String name, String userId, String passKey) {
	this.name = name;
	this.userId = userId;
	this.passKey = passKey;
    }
}
