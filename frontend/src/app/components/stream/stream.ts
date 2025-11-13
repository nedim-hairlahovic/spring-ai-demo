import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat-service';

@Component({
  imports: [CommonModule, FormsModule],
  templateUrl: './stream.html'
})
export class Stream {
  message = '';
  messages: ChatMessage[] = [];
  loading = false;

  constructor(private chatService: ChatService) { }

  sendMessage() {
    const trimmed = this.message.trim();
    if (!trimmed) return;

    this.loading = true;
    this.messages.push({ role: 'user', content: trimmed });
    this.message = '';

    this.chatService.streamChat(trimmed).subscribe({
      next: (chunk) => {
        // Append streamed text as it arrives
        const lastMsg = this.messages[this.messages.length - 1];
        if (lastMsg.role === 'assistant') {
          lastMsg.content += chunk;
        } else {
          this.messages.push({ role: 'assistant', content: chunk });
        }
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

}
