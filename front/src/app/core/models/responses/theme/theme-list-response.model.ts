import { ThemeResponse } from "./theme-response.model";

/**
 * ThemeListResponse represents a response containing a list of themes.
 * 
 * @author Thorekt
 */
export interface ThemeListResponse {
    themes: ThemeResponse[];
}