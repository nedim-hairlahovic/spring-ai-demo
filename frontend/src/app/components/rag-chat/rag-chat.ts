import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat-service';

@Component({
  imports: [CommonModule, FormsModule],
  templateUrl: './rag-chat.html'
})
export class RagChat {

  message = '';
  messages: ChatMessage[] = [];
  loading = false;

  constructor(private chatService: ChatService) { }

  sendMessage() {
    const trimmed = this.message.trim();
    if (!trimmed) return;

    // Add user message
    this.messages.push({ role: 'user', content: trimmed });
    this.message = '';
    this.loading = true;

    // Call backend
    this.chatService.ragChat(trimmed)
      .subscribe({
        next: (res) => {
          this.messages.push({ role: 'assistant', content: res });
          this.loading = false;
        },
        error: (err) => {
          console.error(err);
          this.messages.push({ role: 'assistant', content: 'Error connecting to backend 😕' });
          this.loading = false;
        }
      });
  }

}
