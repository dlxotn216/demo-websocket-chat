package demo.app.file.interfaces.controller;

import demo.app.file.interfaces.dto.FileUploadResponse;
import demo.app.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Created by itaesu on 22/07/2019.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/uploads")
    public ResponseEntity<FileUploadResponse> uploadImage(@RequestParam(name = "image") MultipartFile file) {
        return ok(this.fileService.upload(file));
    }

    @GetMapping("/downloads/{fileId}")
    public ResponseEntity<Resource> downloadImage(HttpServletRequest request,
                                                  @PathVariable("fileId") String fileId) {
        // Load file as Resource
        Resource resource = this.fileService.loadFileAsResource(fileId);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(contentType))
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource
                                     .getFilename() + "\"")
                             .body(resource);
    }
}
