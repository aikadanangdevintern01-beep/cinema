// src/main/java/hunglcb/example/module5/service/FileUploadService.java
package hunglcb.example.module5.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/avatars/";
    private static final List<String> ALLOWED_TYPES = List.of("image/png", "image/jpeg", "image/jpg");

    public String uploadAvatar(MultipartFile file, String username) throws IOException {

        // 1. Kiểm tra loại file
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Chỉ chấp nhận file PNG hoặc JPEG");
        }

        // 2. Kiểm tra kích thước (tối đa 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File quá lớn, tối đa 5MB");
        }

        // 3. Tạo thư mục nếu chưa có
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 4. Tạo tên file an toàn: username + random + đuôi
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = username + "-" + UUID.randomUUID() + "." + ext;

        // 5. Lưu file
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 6. Trả về URL truy cập từ web
        return "/uploads/avatars/" + fileName;
    }
}