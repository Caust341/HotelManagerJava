package util.validator;

public class CustomerValidator {
    public static ValidationResult isCustomerNameValid(String custname) {
        if (custname == null || custname.isEmpty()) {
            return ValidationResult.fail("Customer name cannot be empty");
        }
        if (!custname.matches("^[A-Z][a-z]*(?:\\s[A-Z][a-z]*)*$")) {
            return ValidationResult.fail("Username must have every word start with a capitalized letter");
        }
        return ValidationResult.success();
    }

    public static ValidationResult isCustomerContactValid(String contact) {
        if (contact == null || contact.isEmpty()) {
            return ValidationResult.fail("Customer contact cannot be empty");
        }
        if (contact.length() != 10) {
            return ValidationResult.fail("Customer contact must be 10 length long");
        }
        if (!contact.matches("\\d+")) {
            return ValidationResult.fail("Contact must be made up of only numbers");
        }
        return ValidationResult.success();
    }


}
