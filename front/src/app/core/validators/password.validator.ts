import { AbstractControl, ValidationErrors } from "@angular/forms";

/**
 * Validates that a password meets the required complexity
 * 
 * @param control The form control containing the password to validate.
 * @returns ValidationErrors | null Returns null if the password is valid, otherwise returns an object with the 'passwordInvalid' key set to true.
 * 
 * @author Thorekt
 */
export function passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const hasMinLength = value.length >= 8;
    const hasUpper = /[A-Z]/.test(value);
    const hasLower = /[a-z]/.test(value);
    const hasDigit = /[0-9]/.test(value);
    const hasSpecial = /[^A-Za-z0-9]/.test(value);

    return hasMinLength && hasUpper && hasLower && hasDigit && hasSpecial
        ? null
        : { passwordInvalid: true };
}