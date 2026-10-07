package util.validator;

public class CompanyValidator {
    public static ValidationResult isCompanyName(String name) {
        if (name == null || name.isEmpty()) {
            return ValidationResult.fail("Name cannot be empty");
        }
        return ValidationResult.success();
    }
}
