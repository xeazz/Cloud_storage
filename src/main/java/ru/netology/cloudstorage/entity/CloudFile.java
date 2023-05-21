package ru.netology.cloudstorage.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloudFile {
    @Id
    @Column(name = "file_name")
    private String filename;
    @Lob
    @Column(name = "file_data", columnDefinition = "MEDIUMBLOB")
    private byte[] fileData;
    @Column(name = "size")
    private Long size;
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    private Long userId;
}
