package com.dailycodework.dreamshops.model;

import jakarta.persistence.*;
import lombok.*;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
private Blob image;
private String downloadUrl;

@ManyToOne
@JoinColumn(name="product_id")
private Product product;


}
