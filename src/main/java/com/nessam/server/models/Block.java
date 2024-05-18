package com.nessam.server.models;


import jakarta.persistence.*;



public class Block {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "blocker", nullable = false)
    private String blocker;

    @Column(name = "blocked", nullable = false)
    private String blocked;

    public Block(String blocker, String blocked) {
        this.blocker = blocker;
        this.blocked = blocked;
    }

    public Block() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlocker() {
        return blocker;
    }

    public void setBlocker(String blocker) {
        this.blocker = blocker;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocking) {
        this.blocked = blocking;
    }

    @Override
    public String toString() {
        return "Block{" + "blocker='" + blocker + '\'' + ", blocking='" + blocked + '\'' + '}';
    }

}
