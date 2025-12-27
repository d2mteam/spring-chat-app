package com.project.chatservice.chat.controller;

public class ChatMessageEditRequest {

    private String content;
    private String editorId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }
}
