package uz.pdp.maven.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.maven.dto.SignUpDTO;
import uz.pdp.maven.model.AuthUser;
import uz.pdp.maven.model.Upload;
import uz.pdp.maven.model.UserStatus;
import uz.pdp.maven.repository.UploadRepo;
import uz.pdp.maven.repository.UserRepo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class UserService {

    private static Path rootPath = Path.of("D:\\Odiljon\\PDPAcademy\\8-modul\\ToDoWithSpringSecurity\\src\\main\\resources\\static\\images\\");

    private final UploadRepo uploadRepo;
    private final UserRepo userRepo;

    public UserService(UploadRepo uploadRepo, UserRepo userRepo) {
        this.uploadRepo = uploadRepo;
        this.userRepo = userRepo;
    }

    public void save(SignUpDTO signUpDTO) {

        MultipartFile file = signUpDTO.file();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String generatedName = UUID.randomUUID() + "." + extension;

        try {
            file.transferTo(rootPath.resolve(generatedName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Upload upload = Upload.builder()
                .generatedName(generatedName)
                .extension(extension)
                .originalName(file.getOriginalFilename())
                .size(file.getSize())
                .build();

        int uploadId = uploadRepo.save(upload);

        AuthUser build = AuthUser.builder()
                .name(signUpDTO.name())
                .username(signUpDTO.username())
                .password(signUpDTO.password())
                .isDeleted(false)
                .uploadId(uploadId)
                .status(UserStatus.ACTIVE)
                .build();

        userRepo.save(build);
    }
}
