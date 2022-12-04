package io.springexercise.security.userservice.controler;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.springexercise.security.userservice.Domain.Restaurant;
import io.springexercise.security.userservice.Domain.User;
import io.springexercise.security.userservice.Service.GenerateQr;
import io.springexercise.security.userservice.Service.RestaurantService;
import io.springexercise.security.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qr")
public class QrCodeController {

    private final UserService userService;
    private final RestaurantService restaurantService;

//    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";


//    @GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
//    public void download(
//            @PathVariable("codeText") String codeText,
//            @PathVariable("width") Integer width,
//            @PathVariable("height") Integer height)
//            throws Exception {
//        GenerateQr.generateQRCodeImage(codeText, QR_CODE_IMAGE_PATH);
//    }

    @GetMapping(value = "/genrateQRCode/{username}",produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQRCode(
            @PathVariable("username") String username
           )
            throws Exception {
            Restaurant rest=restaurantService.getRest(username)   ;
        HashMap<String, Serializable> body = new HashMap<String,Serializable>();
        body.put("username", rest.getUsername());
        body.put("restaurantname", rest.getRestaurantName());
        body.put("price", rest.getPrice());
        ObjectMapper mapper = new ObjectMapper();

        String infoJson
                = mapper.writeValueAsString(body);
        BufferedImage image =new BufferedImage(400,300,BufferedImage.TYPE_INT_RGB );
        image=GenerateQr.getQRCodeImage(infoJson);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);

        byte[] encoded = Base64.getEncoder().encode(bos.toByteArray());
        return encoded;
    }
    @GetMapping("/html/{username}")
    @ResponseBody
    public String getHtml(  @PathVariable("username") String username) throws Exception {
        return  new String(generateQRCode(username),"UTF8");
    }




//
//@GetMapping(value = "/genrateQRCode/{username}",produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> generateQRCode(
//            @PathVariable("username") String username
//           )
//            throws Exception {
//            Restaurant rest=restaurantService.getRest(username)   ;
//        HashMap<String, Serializable> body = new HashMap<String,Serializable>();
//        body.put("username", rest.getUsername());
//        body.put("restaurantname", rest.getRestaurantName());
//        body.put("price", rest.getPrice());
//        ObjectMapper mapper = new ObjectMapper();
//
//        String infoJson
//                = mapper.writeValueAsString(body);
////        BufferedImage image =new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB );
//         return successResponse(GenerateQr.getQRCodeImage(infoJson));
//
////        ByteArrayOutputStream bos = new ByteArrayOutputStream();
////        ImageIO.write(image, "png", bos);
//
//    }
//    private ResponseEntity<BufferedImage> successResponse(BufferedImage image) {
//        return new ResponseEntity<>(image, HttpStatus.OK);
//    }
//    @Bean
//    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
//        return new BufferedImageHttpMessageConverter();
//    }

}
