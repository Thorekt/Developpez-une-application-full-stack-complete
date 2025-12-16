import { ArticleResponse } from "./article-response.model";

/**
 * ArticleListResponse represents a response containing a list of articles.
 * 
 * @author Thorekt
 */
export interface ArticleListResponse {
    articles: ArticleResponse[];
}