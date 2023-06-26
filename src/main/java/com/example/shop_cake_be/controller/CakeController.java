package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.models.Cake;
import com.example.shop_cake_be.models.CakePromotion;
import com.example.shop_cake_be.payload.CakePayload;
import com.example.shop_cake_be.service.CakeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

import static com.example.shop_cake_be.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cake")
public class CakeController {
    @Autowired
    CakeService service;

    @PostMapping(value = "/create")
    public Result<?> create(@RequestBody Cake model) throws JsonProcessingException {


        Cake cake = service.create(model);
        if(cake == null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, cake);
    }
    @PostMapping("/getAll")
    public Result<?> getAll(@RequestBody CakePayload filter) {
        Page<Object> page = service.getAllAndSearch(filter);
        if (page == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, page);
        }
    }
    @GetMapping("/findById/{id}")
    public Result<?> findById(@PathVariable("id") Long id) {
        Optional<Cake> cake = service.findById(id);
        if (cake.isEmpty()) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(),SUCCESS,cake);
    }
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable("id") long id, @RequestBody Cake model) throws JsonProcessingException {

        Optional<Cake> c = service.update(id, model);
        if(c.isEmpty()) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(), SUCCESS, c);
    }
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") long id) {
        boolean cat = service.delete(id);
        if(cat) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
//    @GetMapping("/downloadFile/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
//                                                 HttpServletRequest request) {
//        // Load file as Resource
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            //logger.info("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
    @PostMapping("/createCakePromotion")
    public Result<?> createCakePromotion(@RequestBody CakePromotion payLoad) {
        boolean cakePromotion = service.createPromotion(payLoad);
        if(cakePromotion) return Result.success();
        return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
    }
    @PostMapping("/procedure/{id}")
    public Result<?> procedure(@PathVariable("id") long id, @RequestBody CakePayload cakePayload) {
        boolean cake = service.procedure(id, cakePayload);
        if(cake) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
}
