/**
 * CreateArticleRequest represents a request to create a new article.
 * 
 * @author Thorekt
 */
export interface CreateArticleRequest {
    themeUuid: string;
    title: string;
    content: string;
}