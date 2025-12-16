/**
 * CommentResponse represents comment information.
 * 
 * @author Thorekt
 */
export interface CommentResponse {
    uuid: string;
    content: string;
    userUuid: string;
    createdAt: string;
}