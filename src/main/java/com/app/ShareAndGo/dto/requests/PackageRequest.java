package com.app.ShareAndGo.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class PackageRequest {
    private double weight;
    private double length;
    private double width;
    private double height;
    private String receiverPhoneNumber;
}
