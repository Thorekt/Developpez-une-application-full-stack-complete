/**
 * CreateCommentRequest represents a request to create a new comment on an article.
 * 
 * @author Thorekt
 */
export interface CreateCommentRequest {
    articleUuid: string;
    content: string;
}