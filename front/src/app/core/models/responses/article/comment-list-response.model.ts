import { CommentResponse } from "./comment-response.model";

/**
 * CommentListResponse represents a response containing a list of comments.
 * 
 * @author Thorekt
 */
export interface CommentListResponse {
    comments: CommentResponse[];
}