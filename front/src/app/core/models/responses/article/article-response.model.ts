/**
 * ArticleResponse represents article information.
 * 
 * @author Thorekt
 */
export interface ArticleResponse {
    uuid: string;
    title: string;
    content: string;
    userUuid: string;
    themeUuid: string;
    createdAt: string;
}