package com.scm.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.services.ImageService;

@Service
public class ImageServiveImpl implements ImageService {

    private Cloudinary cloudinary;

    

    public ImageServiveImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }



    @Override
    public String uploadImage(MultipartFile contactImage, String fileNAme) {
        // code likhna hai jo image ko upload kara ra ho 

        String fileName = UUID.randomUUID().toString();

        try {
            byte[] data = new byte[contactImage.getInputStream().available()];

            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", fileName
            ));

            return this.urlFromPublicId(fileName);
        } catch (IOException e) {
            
            e.printStackTrace();
            return null;
        }

        // and return hoga url

        
    }



    @Override
    public String urlFromPublicId(String publicId) {
        
        return cloudinary
                  .url()
                  .transformation(
                    new Transformation<>()
                    .width(500)
                    .height(500)
                    .crop("fill")
                  )
                  .generate(publicId);
    }

}
