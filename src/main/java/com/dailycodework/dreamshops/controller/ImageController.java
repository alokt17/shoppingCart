package com.dailycodework.dreamshops.controller;


import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private static final Logger LOGGER = Logger.getLogger(ImageController.class.getName());
    private final IImageService imageService;

    @PostMapping("/upload")

    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId){

        try {
            List<ImageDto>imageDtos=imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!",imageDtos));
        } catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!",e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                .body(resource);
    }

//    @GetMapping("/image/download/{imageId}")
//    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
//        Image image = imageService.getImageById(imageId);
//        if (image == null || image.getImages() == null || image.getFileType() == null) {
//            // Log the error for debugging
//         //   LOGGER.error("Image not found or invalid image data for imageId: {}", imageId);
//            return ResponseEntity.badRequest().body(null);
//        }
//
//       // LOGGER.info("FileType: {}", image.getFileType());
//
//        ByteArrayResource resource = new ByteArrayResource(image.getImages().getBytes(1, (int) image.getImages().length()));
//        MediaType mediaType;
//        try {
//            mediaType = MediaType.parseMediaType(image.getFileType());
//        } catch (IllegalArgumentException e) {
//         //   LOGGER.error("Invalid MIME type: {}", image.getFileType(), e);
//            mediaType = MediaType.APPLICATION_OCTET_STREAM;
//        }
//
//        return ResponseEntity.ok()
//                .contentType(mediaType)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
//                .body(resource);
//    }



    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!", INTERNAL_SERVER_ERROR));
    }


    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.deleteImageById( imageId);
                return ResponseEntity.ok(new ApiResponse("Delete success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
    }
}






