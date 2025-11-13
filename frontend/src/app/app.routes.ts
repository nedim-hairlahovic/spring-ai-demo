import { Routes } from '@angular/router';

import { BasicChat } from './components/basic-chat/basic-chat';
import { HrInternal } from './components/hr-internal/hr-internal';
import { Stream } from './components/stream/stream';
import { StructuredOutput } from './components/structured-output/structured-output';
import { ChatMemory } from './components/chat-memory/chat-memory';
import { RagChat } from './components/rag-chat/rag-chat';
import { ToolsChat } from './components/tools-chat/tools-chat';

export const routes: Routes = [
  { path: '', redirectTo: 'basic-chat', pathMatch: 'full' },
  { path: 'basic-chat', component: BasicChat, title: 'Basic Chat' },
  { path: 'internal-hr', component: HrInternal, title: 'Internal HR' },
  { path: 'stream', component: Stream, title: 'Streaming Chat' },
  { path: 'structured-output', component: StructuredOutput, title: 'Structured Output' },
  { path: 'memory', component: ChatMemory, title: 'Chat Memory' },
  { path: 'rag', component: RagChat, title: 'RAG Example' },
  { path: 'tools', component: ToolsChat, title: 'Tools Example' },
];