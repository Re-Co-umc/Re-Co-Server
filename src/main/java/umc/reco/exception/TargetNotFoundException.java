package umc.reco.exception;

public class TargetNotFoundException extends RuntimeException {
    public TargetNotFoundException() {
    }

    public TargetNotFoundException(String message) {
        super(message);
    }
}
