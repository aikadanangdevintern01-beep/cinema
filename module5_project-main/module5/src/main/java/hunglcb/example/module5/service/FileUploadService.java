// src/main/java/hunglcb/example/module5/service/FileUploadService.java
package hunglcb.example.module5.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileUploadService {

    // THƯ MỤC LƯU ẢNH – TỰ ĐỘNG TẠO NẾU CHƯA CÓ
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/avatars";

    // ĐƯỜNG DẪN TRUY CẬP QUA WEB[](http://localhost:8080/uploads/avatars/...)
    private static final String BASE_URL = "/uploads/avatars";

    public String uploadAvatar(MultipartFile file, String username) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // 1. Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 2. Tạo tên file duy nhất (tránh trùng)
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uniqueFilename = username + "_" + UUID.randomUUID().toString().substring(0, 8)
                    + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + fileExtension;

            // 3. Lưu file vào thư mục
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Trả về URL để lưu vào DB
            return BASE_URL + "/" + uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu ảnh đại diện: " + e.getMessage(), e);
        }
    }

    // BONUS: XÓA ẢNH CŨ KHI CẬP NHẬT (TÙY CHỌN)
    public void deleteOldAvatar(String oldAvatarUrl) {
        if (oldAvatarUrl == null || oldAvatarUrl.isEmpty() || oldAvatarUrl.contains("placeholder")) {
            return;
        }

        try {
            String filename = oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (Exception e) {
            // Không làm gì nếu không xóa được – tránh crash
            System.err.println("Không thể xóa ảnh cũ: " + e.getMessage());
        }
    }
}