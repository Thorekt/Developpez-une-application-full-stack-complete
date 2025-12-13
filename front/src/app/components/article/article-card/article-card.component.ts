import { Component, Input, OnInit } from '@angular/core';
import { ArticleResponse } from 'src/app/core/models/responses/article/article-response.model';

@Component({
  selector: 'app-article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss']
})
export class ArticleCardComponent implements OnInit {
  @Input() article!: ArticleResponse;

  constructor() { }

  ngOnInit(): void {
  }

}
