package com.catpawdogpaw.theartimposter.common.S3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    @Autowired
	private AmazonS3 s3Client;
	
	@Value("${aws.bucketname}")
	private String bucketName;
	@Value("${aws.cloudfront.domain}")
    private String cloudfrontDomain;

	private static final String UPLOAD_PATH = "image/";
	
    public String uploadImage(MultipartFile file) {
		
        // 확장자 추출
    	String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension =originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        UUID uuid = UUID.randomUUID();
        String savedFileName = uuid.toString() + fileExtension;
        
        //파일경로: 업로드폴더 + uuid.확장자
		String filePath = UPLOAD_PATH + savedFileName;
		try {
			s3Client.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = "https://" + cloudfrontDomain + "/" + filePath;
		
		return url;
    }

    public String uploadBase64Image(String base64Image) {
    	  try {
    	        // Base64 문자열이 "data:image/png;base64," 형태인지 확인하고 분리
    	        String[] parts = base64Image.split(",");
    	        if (parts.length < 2) {
    	            throw new IllegalArgumentException("Invalid base64 image data");
    	        }
    	        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
    	        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
    	        String fileExtension = ".png"; // 기본 확장자 설정
    	        UUID uuid = UUID.randomUUID();
    	        String savedFileName = uuid.toString() + fileExtension;

    	        String filePath = UPLOAD_PATH + savedFileName;
    	        ObjectMetadata metadata = new ObjectMetadata();
    	        metadata.setContentLength(decodedBytes.length);
    	        metadata.setContentType("image/png");

    	        s3Client.putObject(new PutObjectRequest(bucketName, filePath, inputStream, metadata));

    	        String url = "https://" + cloudfrontDomain + "/" + filePath;
    	        return url;
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        throw new IllegalArgumentException("Error uploading image: " + e.getMessage());
    	    }
    }
    
}