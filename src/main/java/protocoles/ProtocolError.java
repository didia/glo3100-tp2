package protocoles;

public class ProtocolError extends RuntimeException {

    public ProtocolError(String message) {
	super(message);
    }

    public ProtocolError(Exception e) {
	super(e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -2122587818320593609L;

}
