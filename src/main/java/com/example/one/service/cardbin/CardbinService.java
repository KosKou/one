package com.example.one.service.cardbin;

import com.example.one.entity.Attribute;
import com.example.one.webdto.request.AddAttributeWebRequest;
import com.example.one.webdto.request.AddCardbinWebRequest;
import com.example.one.webdto.request.UpdateAttributeWebRequest;
import com.example.one.webdto.request.UpdateCardbinWebRequest;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface CardbinService {
    //CREATE
    Single addCardbin(AddCardbinWebRequest addCardbinWebRequest);
    Single addCardbinAttribute(Integer cardbinId, AddAttributeWebRequest addAttributeWebRequest);
    //RETRIEVE
    Observable retrieveAllCardbins();
    Observable retrieveAllCardbinAttributes(Integer cardbinId);
    Single retrieveCardbin(Integer id);
    Single retrieveCardbinAttribute(Integer cardbinId, Integer attributeId);
    //UPDATE
    Completable updateCardbin(Integer cardbinId, UpdateCardbinWebRequest updateCardbinWebRequest);
    Completable updateCardbinAttribute(Integer cardbinId, Integer attributeId, UpdateAttributeWebRequest updateAttributeWebRequest);
    //DELETE
    Completable deleteCardbin(Integer cardbinId);
    Completable deleteCardbinAttribute(Integer cardbinId, Integer attributeId);

}
