package error;

public class OverloadException extends RuntimeException {
    public OverloadException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
