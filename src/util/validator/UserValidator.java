package util.validator;

public class UserValidator {
    public static ValidationResult isUserNameValid(String name) {
        if (name == null || name.isEmpty()) {
            return ValidationResult.fail("Username cannot be empty");
        }
        if (!name.matches("^[A-Z][a-z]*(?:\\s[A-Z][a-z]*)*$")) {
            return ValidationResult.fail("Username must have every word start with a capitalized letter");
        }
        return ValidationResult.success();

    }

    public static ValidationResult isPasswordValid(String password) {
        if (password == null || password.isEmpty()) {
            return ValidationResult.fail("Password cannot be empty");
        }
        if (!password.matches(".*[A-Z].*")) {
            return ValidationResult.fail("Password need at least one character to be UPPERCASE");
        }
        if (!password.matches(".*[a-z].*")) {
            return ValidationResult.fail("Password needs at least one LOWERCASE character");
        }
        if (!password.matches(".*\\d.*")) {
            return ValidationResult.fail("Password needs at least one Digit");
        }
        if (password.length() < 8 || password.length() > 20) {
            return ValidationResult.fail("Password need to be between 8 to 20 characters long");
        }
        return ValidationResult.success();
    }
}
