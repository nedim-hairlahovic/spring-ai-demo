import { Component, signal } from '@angular/core';

import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');

  links = [
    { path: '/basic-chat', label: 'Basic Chat' },
    { path: '/internal-hr', label: 'Internal HR' },
    { path: '/stream', label: 'Streaming Chat' },
    { path: '/structured-output', label: 'Structured Output' },
    { path: '/memory', label: 'Chat Memory' },
    { path: '/rag', label: 'RAG Example' },
    { path: '/tools', label: 'Tools Example' },
  ];
}
