package com.app.ShareAndGo.dto.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddCarRequest {
    private String make;
    private String model;
    private String licencePlateNumber;
    private MultipartFile carImage;
}
