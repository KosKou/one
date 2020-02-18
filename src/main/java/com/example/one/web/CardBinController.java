package com.example.one.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.one.entity.Attribute;
import com.example.one.service.cardbin.CardbinService;
import com.example.one.servicedto.CardbinResponse;
import com.example.one.webdto.request.AddAttributeWebRequest;
import com.example.one.webdto.request.AddCardbinWebRequest;
import com.example.one.webdto.request.UpdateAttributeWebRequest;
import com.example.one.webdto.request.UpdateCardbinWebRequest;
import com.example.one.webdto.response.BaseWebResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

/*
 * FLOW: Expose/Controller -> Business/Service -> DAO/Repository -> PROXY API/DB
 * */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cardbins")
public class CardBinController {

    private final CardbinService cardbinService;

    //CREATE
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_STREAM_JSON_VALUE}
    )
    public Single<BaseWebResponse<?>> createCardbin(@RequestBody AddCardbinWebRequest addCardbinWebRequest) {
        return cardbinService.addCardbin(addCardbinWebRequest)
                .subscribeOn(Schedulers.io())
                .map(o -> BaseWebResponse.successNoData());
    }

    @PostMapping(
            value = "/{cardbinId}/attributes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_STREAM_JSON_VALUE}
    )
    public Single<BaseWebResponse<?>> createCardbinAttribute(@RequestBody AddAttributeWebRequest addAttributeWebRequest,
                                                             @PathVariable(value = "cardbinId") Integer cardbinId) {
        return cardbinService.addCardbinAttribute(cardbinId, addAttributeWebRequest)
                .subscribeOn(Schedulers.io())
                .map(o -> BaseWebResponse.successNoData());
    }

    //RETRIEVE
    @GetMapping(
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE
    )
    public Observable<BaseWebResponse<CardbinResponse>> retrieveAllCardbins() {
        return cardbinService.retrieveAllCardbins()
                .subscribeOn(Schedulers.io())
                .map(o -> BaseWebResponse.successWithData(o));
    }

    @GetMapping(
            value = "/{cardbinId}",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE
    )
    public Single<BaseWebResponse<CardbinResponse>> retrieveCardbin(@PathVariable(value = "cardbinId") Integer cardbinId) {
        return cardbinService.retrieveCardbin(cardbinId)
                .subscribeOn(Schedulers.io())
                .map(o -> BaseWebResponse.successWithData(o));
    }

    @GetMapping(
            value = "/{cardbinId}/attributes",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE
    )
    public Observable<BaseWebResponse<Attribute>>  retrieveCardbinAttributes(@PathVariable(value = "cardbinId") Integer cardbinId) {
        return cardbinService.retrieveAllCardbinAttributes(cardbinId)
                .subscribeOn(Schedulers.io())
                .map(o -> BaseWebResponse.successWithData(o));
    }

    @GetMapping(
            value = "/{cardbinId}/attributes/{attributeId}",
            produces = {MediaType.APPLICATION_STREAM_JSON_VALUE}
    )
    public Single<BaseWebResponse<Attribute>> retrieveCardbinAttribute(
            @PathVariable(value = "cardbinId") Integer cardbinId,
            @PathVariable(value = "attributeId") Integer attributeId) {
        return cardbinService.retrieveCardbinAttribute(cardbinId, attributeId)
                .subscribeOn(Schedulers.io())
                .map(o -> BaseWebResponse.<Attribute>successWithData(o));
    }

    //UPDATE
    @PutMapping(
            value = "/{cardbinId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_STREAM_JSON_VALUE}
    )
    public Single<BaseWebResponse<?>> updateCardbin(@PathVariable(value = "cardbinId") Integer cardbinId,
                                                    @RequestBody UpdateCardbinWebRequest updateCardbinWebRequest) {
        return cardbinService.updateCardbin(cardbinId, updateCardbinWebRequest)
                .subscribeOn(Schedulers.io())
                .toSingle(() -> BaseWebResponse.successNoData());
    }

    @PutMapping(
            value = "/{cardbinId}/attributes/{attributeId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_STREAM_JSON_VALUE}
    )
    public Single<BaseWebResponse<?>> updateCardbinAttribute(@PathVariable(value = "cardbinId") Integer cardbinId,
                                                             @PathVariable(value = "attributeId") Integer attributeId,
                                                             @RequestBody UpdateAttributeWebRequest updateAttributeWebRequest) {
        return cardbinService.updateCardbinAttribute(cardbinId, attributeId, updateAttributeWebRequest)
                .subscribeOn(Schedulers.io())
                .toSingle(() -> BaseWebResponse.successNoData());
    }
    //DELETE

    @DeleteMapping(
            value = "/{cardbinId}",
            produces = {MediaType.APPLICATION_STREAM_JSON_VALUE}
    )
    public Single<BaseWebResponse<?>> deleteCardbin(@PathVariable(value = "cardbinId") Integer cardbinId) {
        return cardbinService.deleteCardbin(cardbinId)
                .subscribeOn(Schedulers.io())
                .toSingle(() -> BaseWebResponse.successNoData());
    }

    @DeleteMapping(
            value = "/{cardbinId}/attributes/{attributeId}",
            produces = {MediaType.APPLICATION_STREAM_JSON_VALUE}
    )
    public Single<BaseWebResponse<?>> deleteCardbinAttribute(@PathVariable(value = "cardbinId") Integer cardbinId,
                                                             @PathVariable(value = "attributeId") Integer attributeId) {
        return cardbinService.deleteCardbinAttribute(cardbinId, attributeId)
                .subscribeOn(Schedulers.io())
                .toSingle(() -> BaseWebResponse.successNoData());
    }
}