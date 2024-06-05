package com.nessam.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable {
    @Column(name = "message-id")
    private String id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "text")
    private String text;

    @Column(name = "createdat")
    private String createdAt;

    public Message () {

    }


    public Message(String id, String sender, String receiver, String text, long createdAt) {
        this.id = id;
        this.receiver = receiver;
        this.sender = sender;
        this.text = text;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.createdAt = formatter.format(new Date());
    }

    public Message(String id, String sender, String receiver, String text) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.createdAt = formatter.format(new Date());
    }

    public Message(String sender, String receiver, String text) {
        this.id = sender + System.currentTimeMillis();
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.createdAt = formatter.format(new Date());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!createdAt.equals(message.createdAt)) return false;
        if (!id.equals(message.id)) return false;
        if (!receiver.equals(message.receiver)) return false;
        return text.equals(message.text);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + receiver.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (int) (Integer.parseInt(createdAt) ^ (Integer.parseInt(createdAt)));
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return (int)(Integer.parseInt(((Message)o).createdAt) -  Integer.parseInt(createdAt));
    }
}
