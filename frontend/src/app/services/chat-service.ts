import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SportTeam } from '../models/sport-team';

@Injectable({
  providedIn: 'root',
})
export class ChatService {

  private baseUrl = 'http://localhost:8080/api/chat';

  constructor(private http: HttpClient) { }

  /**
   * Generic chat endpoint – simple OpenAI integration
   */
  chat(message: string): Observable<string> {
    const params = new HttpParams().set('message', message.trim());
    return this.http.get(`${this.baseUrl}/basic`, { params, responseType: 'text' });
  }

  /**
   * HR Assistant – domain-specific chatbot for internal HR policies
   */
  hrInternal(message: string): Observable<string> {
    const params = new HttpParams().set('message', message.trim());
    return this.http.get(`${this.baseUrl}/hr-internal`, { params, responseType: 'text' });
  }

  streamChat(message: string): Observable<string> {
    return new Observable((observer) => {
      fetch(`${this.baseUrl}/stream?message=${encodeURIComponent(message.trim())}`)
        .then((response) => {
          const reader = response.body?.getReader();
          const decoder = new TextDecoder();

          const readChunk = () => {
            reader?.read().then(({ done, value }) => {
              if (done) {
                observer.complete();
                return;
              }
              const chunk = decoder.decode(value, { stream: true });
              observer.next(chunk);
              readChunk();
            });
          };

          readChunk();
        })
        .catch((err) => observer.error(err));
    });
  }

  structuredOutput(message: string): Observable<SportTeam> {
    const params = new HttpParams().set('message', message.trim());
    return this.http.get<SportTeam>(`${this.baseUrl}/structured-output`, { params });
  }

  chatMemory(message: string, sessionId: string): Observable<string> {
    const headers = new HttpHeaders({
      'X-Session-Id': sessionId
    });
    const params = new HttpParams().set('message', message.trim());
    return this.http.get(`${this.baseUrl}/memory`, { params, headers, responseType: 'text' });
  }

  ragChat(message: string): Observable<string> {
    const params = new HttpParams().set('message', message.trim());
    return this.http.get(`${this.baseUrl}/rag`, { params, responseType: 'text' });
  }

  toolsChat(message: string, sessionId: string): Observable<string> {
    const headers = new HttpHeaders({
      'X-Session-Id': sessionId
    });
    const params = new HttpParams().set('message', message.trim());
    return this.http.get(`${this.baseUrl}/tools`, { params, headers, responseType: 'text' });
  }
}
