package app;

public class User {
    public final String name;
    public final String userId;
    public final String passKey;

    public User(String name, String userId, String passKey) {
	this.name = name;
	this.userId = userId;
	this.passKey = passKey;
    }
}
