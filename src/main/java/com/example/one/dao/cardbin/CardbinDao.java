package com.example.one.dao.cardbin;

import com.example.one.entity.Attribute;
import com.example.one.entity.CardBin;
import com.example.one.servicedto.CardbinResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface CardbinDao {
    //CREATE
    Single<CardBin> addCardbinInRepository(CardBin cardbin);
    Single<CardBin> addCardbinAttributeInRepository(Integer cardbinId, Attribute attribute);
    //RETRIEVE
    Observable<CardbinResponse> retrieveAllCardbinsFromRepository();
    Observable<Attribute> retrieveAllCardbinAttributesFromRepository(Integer cardbinId);
    Single<CardbinResponse> retrieveCardbinFromRepository(Integer id);
    Single<Attribute> retrieveCardbinAttributeFromRepository(Integer cardbinId, Integer attributeId);
    //UPDATE
    Completable updateCardbinInRepository(CardBin cardbin);
    Completable updateCardbinAttributeInRepository(Attribute attribute);
    //DELETE
    Completable deleteCardbinFromRepository(Integer cardbinId);
    Completable deleteCardbinAttributeFromRepository(Integer attributeId);

}
