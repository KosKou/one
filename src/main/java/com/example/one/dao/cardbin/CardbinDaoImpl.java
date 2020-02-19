package com.example.one.dao.cardbin;

import com.example.one.entity.Attribute;
import com.example.one.entity.CardBin;
import com.example.one.repository.AttributeRepository;
import com.example.one.repository.CardBinRepository;
import com.example.one.servicedto.CardbinResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
/*
* Banner Gonzales: "DAO layer is the layer that connects with the data of DB/API".
* This replace the repository layer following the flow of DAO->Proxy->Business API.
* FLOW: Expose/Controller -> Business/Service -> DAO/Repository -> PROXY API/DB
* I'm creating this layer for understandability purposes only "Methods will return null".
* */

//@Repository
@RequiredArgsConstructor
public class CardbinDaoImpl implements CardbinDao {

    private final AttributeRepository attributeRepository;

    private final CardBinRepository cardbinRepository;

    @Override
    public Single<CardBin> addCardbinInRepository(CardBin cardbin) {
        return Single.just(cardbinRepository.save(cardbin));
    }

    @Override
    public Single<CardBin> addCardbinAttributeInRepository(CardBin cardbin) {
        return Single.just(cardbinRepository.save(cardbin));
    }

    @Override
    public Observable<CardbinResponse> retrieveAllCardbinsFromRepository() {
        return null;
    }

    @Override
    public Observable<Attribute> retrieveAllCardbinAttributesFromRepository(Integer cardbinId) {
        return null;
    }

    @Override
    public Single<CardbinResponse> retrieveCardbinFromRepository(Integer id) {
        return null;
    }

    @Override
    public Single<Attribute> retrieveCardbinAttributeFromRepository(Integer cardbinId, Integer attributeId) {
        return null;
    }

    @Override
    public Completable updateCardbinInRepository(CardBin cardbin) {
        return null;
    }

    @Override
    public Completable updateCardbinAttributeInRepository(Attribute attribute) {
        return null;
    }

    @Override
    public Completable deleteCardbinFromRepository(Integer cardbinId) {
        return null;
    }

    @Override
    public Completable deleteCardbinAttributeFromRepository(Integer attributeId) {
        return null;
    }
}
