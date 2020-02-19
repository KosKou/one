package com.example.one.service.cardbin;

import com.example.one.entity.Attribute;
import com.example.one.entity.CardBin;
import com.example.one.repository.AttributeRepository;
import com.example.one.repository.CardBinRepository;
import com.example.one.servicedto.CardbinResponse;
import com.example.one.webdto.request.AddAttributeWebRequest;
import com.example.one.webdto.request.AddCardbinWebRequest;
import com.example.one.webdto.request.UpdateAttributeWebRequest;
import com.example.one.webdto.request.UpdateCardbinWebRequest;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/*
* Why does you use findById() instead of getOne() Method?
* "getOne gets you a reference, but not the actual entity.
* Get one does noe fetch the object from the DB.
* It just creates an object with the ID you specified." - StackOverflow
* */

@Service
@Transactional //This annotation allows the communication between the application and LocalDatabase
@RequiredArgsConstructor //Creates the constructor/injection to all the final variables.
public class CardbinServiceImpl implements CardbinService{

    private final CardBinRepository cardbinRepository;
    private final AttributeRepository attributeRepository;

    private boolean findCardbin(CardBin cardbin){
        return cardbin.getState().equals("ACTIVE");
    }

    @Override
    public Single<CardBin> addCardbin(AddCardbinWebRequest addCardbinWebRequest) {
        return addCardbinToRepository(addCardbinWebRequest);
    }

    private Single<CardBin> addCardbinToRepository(AddCardbinWebRequest addCardbinWebRequest){
        return Single.just(cardbinRepository.save(toCardbin(addCardbinWebRequest)));
    }

    private CardBin toCardbin(AddCardbinWebRequest addCardbinWebRequest){
        return CardBin.builder()
                .binNumber(addCardbinWebRequest.getBinNumber())
                .binType(addCardbinWebRequest.getBinType())
                .state("ACTIVE")
                .build();
    }

    @Override
    public Single<CardBin> addCardbinAttribute(Integer cardbinId, AddAttributeWebRequest addAttributeWebRequest) {
        return Single.fromCallable(() -> cardbinRepository.findById(cardbinId).orElse(null))
                .map(cardbin -> addAttributeList(cardbin, toAttribute(addAttributeWebRequest)))
                .doOnSuccess(cardbinRepository::save)
                .doOnError(throwable -> new EntityNotFoundException());
    }

    private CardBin addAttributeList(CardBin cardbin, Attribute attribute){
        List<Attribute> attributeList = cardbin.getAttributes();
        attributeList.add(attribute);
        cardbin.setAttributes(attributeList);
        return cardbin;
    }

    private Attribute toAttribute(AddAttributeWebRequest addAttributeWebRequest){
        return Attribute.builder()
                .key(addAttributeWebRequest.getKey())
                .value(addAttributeWebRequest.getValue())
                .state("ACTIVE")
                .build();
    }

    @Override
    public Observable<CardbinResponse> retrieveAllCardbins() {
        return Observable.fromIterable(cardbinRepository.findAll())
                .map(this::toCardbinResponse)
                .filter(cardbinResponse -> cardbinResponse.getState().equals("ACTIVE"));
    }

    private CardbinResponse toCardbinResponse(CardBin cardbin){
        return CardbinResponse.builder()
                .id(cardbin.getId())
                .binNumber(cardbin.getBinNumber())
                .binType(cardbin.getBinType())
                .attributes(cardbin.getAttributes().size())
                .state(cardbin.getState())
                .build();
    }

    @Override
    public Observable<Attribute> retrieveAllCardbinAttributes(Integer cardbinId) {
        return Observable.fromCallable(() -> cardbinRepository.findById(cardbinId).orElse(null))
                .map(CardBin::getAttributes)
                .map(attributes -> attributes
                        .stream()
                        .filter(attribute -> attribute.getState().equals("ACTIVE"))
                        .collect(Collectors.toList()))
                .flatMapIterable(atrib->atrib)
                .doOnError(throwable -> new EntityNotFoundException());
    }

    @Override
    public Single<CardbinResponse> retrieveCardbin(Integer cardbinId) {
        return Single.fromCallable(() -> cardbinRepository.findById(cardbinId).orElse(null))
                .flatMap(cardbin -> {
                    if (findCardbin(cardbin))
                        return Single.just(toCardbinResponse(cardbin));
                    else
                        return Single.error(new EntityNotFoundException());
                });
    }

    @Override
    public Single<Attribute> retrieveCardbinAttribute(Integer cardbinId, Integer attributeId) {
        return Single.fromCallable(() -> cardbinRepository.findById(cardbinId).orElse(null))
                .map(CardBin::getAttributes)
                .flatMapObservable(Observable::fromIterable)
                .filter(attribute -> attribute.getId() == attributeId
                        && attribute.getState().equals("ACTIVE"))
                .firstOrError();
    }

    @Override
    public Completable updateCardBin(Integer cardBinId, UpdateCardbinWebRequest updateCardbinWebRequest) {
        return Maybe.fromCallable(() -> cardbinRepository.findById(cardBinId).orElse(null))
                .map(cardBin -> toUpdateCardBin(cardBin, updateCardbinWebRequest))
                .doOnSuccess(cardbinRepository::save)
                .switchIfEmpty(Maybe.error(EntityNotFoundException::new))
                .ignoreElement();
    }

    private CardBin toUpdateCardBin(CardBin cardbin, UpdateCardbinWebRequest updateCardbinWebRequest){
        return CardBin.builder()
                .id(cardbin.getId())
                .binType(updateCardbinWebRequest.getBinType())
                .binNumber(updateCardbinWebRequest.getBinNumber())
                .state(cardbin.getState())
                .attributes(cardbin.getAttributes())
                .build();
    }

    @Override
    public Completable updateCardBinAttribute(Integer cardBinId, Integer attributeId, UpdateAttributeWebRequest updateAttributeWebRequest) {
        return updateCardBinAttributeInRepository(cardBinId,
                toAttribute(attributeRepository.findById(attributeId).orElse(null)
                        , updateAttributeWebRequest));
    }

    private Completable updateCardBinAttributeInRepository(Integer cardBinId, Attribute attribute){
        return Maybe.fromCallable(() -> cardbinRepository.findById(cardBinId).orElse(null))
                .doOnSuccess(cardBin -> attributeRepository.save(attribute))
                .ignoreElement();
    }

    private Attribute toAttribute(Attribute attribute, UpdateAttributeWebRequest updateAttributeWebRequest){
        return Attribute.builder()
                .id(attribute.getId())
                .key(updateAttributeWebRequest.getKey())
                .value(updateAttributeWebRequest.getValue())
                .state(attribute.getState())
                .build();
    }

    @Override
    public Completable deleteCardBin(Integer cardBinId) {
        return Maybe.fromCallable(() -> cardbinRepository.getOne(cardBinId))
                .map(cardBin -> {
                    cardBin.setState("INACTIVE");
                    return cardBin;
                })
                .doOnSuccess(cardbinRepository::save)
                .ignoreElement();
    }

    @Override
    public Completable deleteCardBinAttribute(Integer cardBinId, Integer attributeId) {
        return Maybe.fromCallable(() -> cardbinRepository.findById(cardBinId).orElse(null))
                .map(cardBin -> toDeleteCardBinAttributes(cardBin, attributeId))
                .doOnSuccess(cardbinRepository::save)
                .ignoreElement();
    }

    private CardBin toDeleteCardBinAttributes(CardBin cardbin, Integer attributeId){
        cardbin.setAttributes(cardbin.getAttributes().stream()
                .filter(attribute -> attribute.getId() != attributeId)
                .collect(Collectors.toList()));
        return cardbin;
    }
}