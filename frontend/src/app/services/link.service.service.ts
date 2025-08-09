// link.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface LinkCreateRequest {
  targetUrl: string;
}

interface LinkResponse {
  code: string;
  shortUrl: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class LinkService {
  constructor(private http: HttpClient) { }

  createLink(targetUrl: string): Observable<LinkResponse> {
    return this.http.post<LinkResponse>('/api/links', { targetUrl });
  }
}