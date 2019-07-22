package demo.app.file.service;

import demo.app.file.interfaces.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static demo.app.file.interfaces.dto.FileUploadResponse.from;
import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * Created by itaesu on 22/07/2019.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private static final String DIRECTORY_PATH = "uploads";
//    private final ServletContext servletContext;

    public FileUploadResponse upload(MultipartFile multipartFile) {
        String fileId = UUID.randomUUID().toString() + "." + getExtension(multipartFile.getOriginalFilename());
        Path directoryPath = getRootPath().resolve(DIRECTORY_PATH);
        if (!directoryPath.toFile().exists() && !directoryPath.toFile().mkdirs()) {
            throw new IllegalStateException("디렉토리를 생성할 수 없습니다.");
        }

        Path filePath = directoryPath.resolve(fileId);
        try {
            multipartFile.transferTo(filePath.toFile());
        } catch (IOException e) {
            log.error("파일 생성 중 예외", e);
            throw new IllegalStateException("파일 생성 중 예외 발생", e);
        }
        return from(multipartFile.getOriginalFilename(), filePath.toString(), fileId);
    }

    public Resource loadFileAsResource(String fileId) {
        Path filePath = getRootPath().resolve(DIRECTORY_PATH).resolve(fileId);
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("존재하지 않는 경로 [" + filePath.toString() + "]");
        }
    }


    //두 대의 서버를 띄울 경우 공유 파일 서버가 없어서 임시로..
    private Path getRootPath() {
        //        return Paths.get(this.servletContext.getRealPath("/"));
        return Paths.get("/Users/itaesu/Desktop/Work/deploy");
    }
}
