package com.example.startup.payload.res;

import lombok.*;
@Builder
public record ResPageable<T> (
        int page,
        int size,
        int totalPage,
        long totalElements,
        T data
){
}
