import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { ArticleListByThemeUuidsInOrder } from '../../models/requests/theme/article-list-by-theme-uiids-in-order.model';
import { Observable } from 'rxjs';
import { ArticleListResponse } from '../../models/responses/article/article-list-response.model';
import { ErrorResponse } from '../../models/responses/error-response.model';
import { ArticleResponse } from '../../models/responses/article/article-response.model';
import { SuccessResponse } from '../../models/responses/success-response.model';
import { CreateArticleRequest } from '../../models/requests/article/create-article-request.model';
import { CommentListResponse } from '../../models/responses/article/comment-list-response.model';
import { CreateCommentRequest } from '../../models/requests/article/create-comment-request.model';

/**
 * ArticleService handles article-related operations such as fetching articles and managing comments.
 * 
 * @extends ApiService
 * @providedIn root
 * 
 * @author Thorekt
 */
@Injectable({
  providedIn: 'root'
})
export class ArticleService extends ApiService {

  private prefix = '/article';

  /**
   * Retrieves a list of articles based on the provided theme UUIDs in a specified order.
   * 
   * @param data ArticleListByThemeUuidsInOrder containing the theme UUIDs and the order.
   * @returns Observable<ArticleListResponse | ErrorResponse> containing the list of articles or an error response.
   */
  getArticlesByThemeUuidsInOrder(data: ArticleListByThemeUuidsInOrder): Observable<ArticleListResponse | ErrorResponse> {
    const themeUuids: string = data.themeUuids.join(',');
    const order: string = data.order;
    return this.http.get<ArticleListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/?theme_uuids=${themeUuids}&order=${order}`);
  }

  /**
   * Retrieves a specific article by its UUID.
   * 
   * @param articleUuid string representing the UUID of the article to be fetched.
   * @returns Observable<ArticleResponse | ErrorResponse> containing the article details or an error response.
   */
  getArticleByUuid(articleUuid: string): Observable<ArticleResponse | ErrorResponse> {
    return this.http.get<ArticleResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${articleUuid}`);
  }

  /**
   * Creates a new article.
   * 
   * @param data CreateArticleRequest containing the article details.
   * @returns Observable<SuccessResponse | ErrorResponse> indicating the success or failure of the article creation.
   */
  createArticle(data: CreateArticleRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.post<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/`, data);
  }

  /**
   * Retrieves comments for a specific article.
   * 
   * @param articleUuid string representing the UUID of the article whose comments are to be fetched.
   * @returns Observable<CommentListResponse | ErrorResponse> containing the list of comments or an error response.
   */
  getCommentFromArticle(articleUuid: string): Observable<CommentListResponse | ErrorResponse> {
    return this.http.get<CommentListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${articleUuid}/comments`);
  }

  /**
   * Creates a new comment for a specific article.
   * 
   * @param data CreateCommentRequest containing the comment details.
   * @returns Observable<SuccessResponse | ErrorResponse> indicating the success or failure of the comment creation.
   */
  createComment(data: CreateCommentRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.post<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/comment`, data);
  }
}