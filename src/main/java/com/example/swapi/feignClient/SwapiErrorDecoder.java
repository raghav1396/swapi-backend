package com.example.swapi.feignClient;

import com.example.swapi.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SwapiErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder delegate = new Default();

  @SneakyThrows
  @Override
  public Exception decode(String methodKey, Response response) {

    if (HttpStatus.valueOf(response.status()).is4xxClientError()) {
      if(response.status() == HttpStatus.NOT_FOUND.value()){
        throw new NotFoundException("Error Not Found");
      }
      throw new RuntimeException("FeignClient failed with 4XX Error");
    }

    if (HttpStatus.valueOf(response.status()).is5xxServerError()) {
      throw new RuntimeException("FeignClient failed with 5XX Error");
    }
    return delegate.decode(methodKey, response);
  }

}
