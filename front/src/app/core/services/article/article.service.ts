import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { ArticleLlistByThemeUuidsInOrder } from '../../models/requests/theme/article-list-by-theme-uiids-in-order.model';
import { Observable } from 'rxjs';
import { ArticleListResponse } from '../../models/responses/article/article-list-response.model';
import { ErrorResponse } from '../../models/responses/error-response.model';
import { ArticleResponse } from '../../models/responses/article/article-response.model';
import { SuccessResponse } from '../../models/responses/success-response.model';
import { CreateArticleRequest } from '../../models/requests/article/create-article-request.model';
import { CommentListResponse } from '../../models/responses/article/comment-list-response.model';
import { CreateCommentRequest } from '../../models/requests/article/create-comment-request.model';

@Injectable({
  providedIn: 'root'
})
export class ArticleService extends ApiService {

  private prefix = '/article';

  getArticlesByThemeUuidsInOrder(data: ArticleLlistByThemeUuidsInOrder): Observable<ArticleListResponse | ErrorResponse> {
    const themeUuids: string = data.themeUuids.join(',');
    const order: string = data.order;
    return this.http.get<ArticleListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/?theme_uuids=${themeUuids}&order=${order}`);
  }

  getArticleByUuid(articleUuid: string): Observable<ArticleResponse | ErrorResponse> {
    return this.http.get<ArticleResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${articleUuid}`);
  }

  createArticle(data: CreateArticleRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.post<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/`, data);
  }

  getCommentFromArticle(articleUuid: string): Observable<CommentListResponse | ErrorResponse> {
    return this.http.get<CommentListResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/${articleUuid}/comments`);
  }

  createComment(date: CreateCommentRequest): Observable<SuccessResponse | ErrorResponse> {
    return this.http.post<SuccessResponse | ErrorResponse>(`${this.baseUrl}${this.prefix}/comment`, date);
  }
}