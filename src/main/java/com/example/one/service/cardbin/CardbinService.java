package com.example.one.service.cardbin;

import com.example.one.entity.Attribute;
import com.example.one.entity.CardBin;
import com.example.one.servicedto.CardbinResponse;
import com.example.one.webdto.request.AddAttributeWebRequest;
import com.example.one.webdto.request.AddCardbinWebRequest;
import com.example.one.webdto.request.UpdateAttributeWebRequest;
import com.example.one.webdto.request.UpdateCardbinWebRequest;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface CardbinService {
    //CREATE
    Single<CardBin> addCardbin(AddCardbinWebRequest addCardbinWebRequest);
    Single<CardBin> addCardbinAttribute(Integer cardbinId, AddAttributeWebRequest addAttributeWebRequest);
    //RETRIEVE
    Observable<CardbinResponse> retrieveAllCardbins();
    Observable<Attribute> retrieveAllCardbinAttributes(Integer cardbinId);
    Single<CardbinResponse> retrieveCardbin(Integer id);
    Single<Attribute> retrieveCardbinAttribute(Integer cardbinId, Integer attributeId);
    //UPDATE
    Completable updateCardBin(Integer cardbinId, UpdateCardbinWebRequest updateCardbinWebRequest);
    Completable updateCardBinAttribute(Integer cardbinId, Integer attributeId, UpdateAttributeWebRequest updateAttributeWebRequest);
    //DELETE
    Completable deleteCardBin(Integer cardbinId);
    Completable deleteCardBinAttribute(Integer cardbinId, Integer attributeId);

}
