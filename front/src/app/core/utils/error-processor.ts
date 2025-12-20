import { Injectable } from "@angular/core";

enum ErrorMessage {
    "RESOURCE_NOT_FOUND" = "Resultat de la recherche introuvable.",
    "INVALID_FORMAT" = "Le format fourni est invalide.",
    "BAD_CREDENTIALS" = "Les identifiants fournis sont incorrects.",
    "USERNAME_ALREADY_IN_USE" = "Le nom d'utilisateur est déjà utilisé.",
    "EMAIL_AND_USERNAME_ALREADY_IN_USE" = "L'email et le nom d'utilisateur sont déjà utilisés.",
    "EMAIL_ALREADY_IN_USE" = "L'email est déjà utilisé.",
    "INTERNAL_SERVER_ERROR" = "Une erreur interne est survenue sur le serveur. Veuillez réessayer plus tard.",

}


/**
 * ErrorProcessor is responsible for processing and handling errors in Http Responses throughout the application.
 * 
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
    providedIn: 'root'
})
export class ErrorProcessor {

    /**
     * Processes the error code and returns a user-friendly message.
     * @param errorCode The error code to process.
     * @returns A user-friendly error message.
     */
    processError(errorCode: string): string {
        return ErrorMessage[errorCode as keyof typeof ErrorMessage] || "Une erreur inconnue est survenue.";
    }

}