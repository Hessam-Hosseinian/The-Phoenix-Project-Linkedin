package com.nessam.server.utils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE_DATA")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileData {

    @Id
    private Long id;
    private String name;
    private String type;
    private String filePath;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
