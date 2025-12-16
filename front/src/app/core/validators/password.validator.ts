import { AbstractControl, ValidationErrors } from "@angular/forms";

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