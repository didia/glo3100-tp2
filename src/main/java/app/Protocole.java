package app;

public interface Protocole {
    public void executeEtape(EtapeProtocole etape, String message);

    public void completeEtape(EtapeProtocole etape, String message);

    public ProtocoleTypes reqProtocoleType();
}
