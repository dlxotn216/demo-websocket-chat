package demo.app.file.interfaces.dto;

import lombok.Getter;

/**
 * Created by itaesu on 22/07/2019.
 */
@Getter
public class FileUploadResponse {
    private String fileName;
    private String downloadPath;
    private String fileId;

    public static FileUploadResponse from(String fileName, String downloadPath, String fileId) {
        FileUploadResponse response = new FileUploadResponse();
        response.fileName = fileName;
        response.downloadPath = downloadPath;
        response.fileId = fileId;
        return response;
    }
}
