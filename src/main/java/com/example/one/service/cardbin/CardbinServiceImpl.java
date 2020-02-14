package com.example.one.service.cardbin;

import com.example.one.entity.Attribute;
import com.example.one.entity.Cardbin;
import com.example.one.repository.AttributeRepository;
import com.example.one.repository.CardbinRepository;
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
import javax.smartcardio.Card;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CardbinServiceImpl implements CardbinService{

    private final CardbinRepository cardbinRepository;
    private final AttributeRepository attributeRepository;


    private List<Cardbin> findCardbins(List<Cardbin> cardbinList){
        return cardbinList
                .stream()
                .filter(cardbin -> findCardbin(cardbin))
                .collect(Collectors.toList());
    }

    private boolean findCardbin(Cardbin cardbin){
        return cardbin.getState().equals("ACTIVE");
    }

    @Override
    public Single addCardbin(AddCardbinWebRequest addCardbinWebRequest) {
        return addCardbinToRepository(addCardbinWebRequest);
    }

    private Single<Cardbin> addCardbinToRepository(AddCardbinWebRequest addCardbinWebRequest){
        return Single.just(cardbinRepository.save(toCardbin(addCardbinWebRequest)));
    }

    private Cardbin toCardbin(AddCardbinWebRequest addCardbinWebRequest){
        return Cardbin.builder()
                .binNumber(addCardbinWebRequest.getBinNumber())
                .binType(addCardbinWebRequest.getBinType())
                .state("ACTIVE")
                .build();
    }

    @Override
    public Single addCardbinAttribute(Integer cardbinId, AddAttributeWebRequest addAttributeWebRequest) {
        return Single.fromCallable(() -> cardbinRepository.findById(cardbinId).get())
                .map(cardbin -> addAttributeList(cardbin, toAttribute(addAttributeWebRequest)))
                .doOnSuccess(cardbin -> cardbinRepository.save(cardbin))
                .doOnError(throwable -> new EntityNotFoundException());
    }

    private Cardbin addAttributeList(Cardbin cardbin, Attribute attribute){
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
    public Observable<List<CardbinResponse>> retrieveAllCardbins() {
        return Observable.fromCallable(() -> cardbinRepository.findAll())
                .map(this::toCardbinResponseList)
                .map(cardbinResponses -> cardbinResponses.stream()
                        .filter(cardbinResponse -> cardbinResponse.getState().equals("ACTIVE"))
                        .collect(Collectors.toList()));
    }

    private List<CardbinResponse> toCardbinResponseList(List<Cardbin> cardbinList){
        return cardbinList
                .stream()
                .map(this::toCardbinResponse)
                .collect(Collectors.toList());
    }

    private CardbinResponse toCardbinResponse(Cardbin cardbin){
        return CardbinResponse.builder()
                .id(cardbin.getId())
                .binNumber(cardbin.getBinNumber())
                .binType(cardbin.getBinType())
                .attributes(cardbin.getAttributes().size())
                .state(cardbin.getState())
                .build();
    }

    @Override
    public Observable<List<Attribute>> retrieveAllCardbinAttributes(Integer cardbinId) {
        return Observable.fromCallable(() -> cardbinRepository.findById(cardbinId))
                .map(cardbin -> cardbin.get().getAttributes())
                .map(attributes -> attributes.stream().filter(attribute -> attribute.getState().equals("ACTIVE"))
                .collect(Collectors.toList()))
                .doOnError(throwable -> new EntityNotFoundException());
//           return Observable.fromCallable(() -> cardbinRepository.findById(cardbinId)
//                   .filter(cardbin -> cardbin
//                           .getAttributes()
//                           .stream()
//                           .filter(attribute -> attribute.getState().equals("ACTIVE"))
//                           .allMatch(Objects::nonNull)
//                   )
//           );
    }

    @Override
    public Single<CardbinResponse> retrieveCardbin(Integer cardbinId) {
        return Single.fromCallable(() -> cardbinRepository.getOne(cardbinId))
                .flatMap(cardbin -> {
                    if (findCardbin(cardbin))
                        return Single.just(toCardbinResponse(cardbin));
                    else
                        return Single.error(new EntityNotFoundException());
                });
    }

    @Override
    public Single<Attribute> retrieveCardbinAttribute(Integer cardbinId, Integer attributeId) {
        return Single.just(cardbinRepository.findById(cardbinId).get().
                getAttributes()
                .stream()
                .filter(attribute -> attribute.getId() == attributeId))
                .map(attributeStream -> attributeStream.findFirst().get())
                .doOnError(throwable -> new EntityNotFoundException());
    }

    @Override
    public Completable updateCardbin(Integer cardbinId, UpdateCardbinWebRequest updateCardbinWebRequest) {
        return Maybe.fromCallable(() -> cardbinRepository.getOne(cardbinId))
                .map(cardbin -> toUpdateCardbin(cardbin, updateCardbinWebRequest))
                .doOnSuccess(cardbin -> cardbinRepository.save(cardbin))
                .switchIfEmpty(Maybe.error(() -> new EntityNotFoundException()))
                .ignoreElement();
    }

    private Cardbin toUpdateCardbin(Cardbin cardbin, UpdateCardbinWebRequest updateCardbinWebRequest){
        return Cardbin.builder()
                .id(cardbin.getId())
                .binType(updateCardbinWebRequest.getBinType())
                .binNumber(updateCardbinWebRequest.getBinNumber())
                .state(cardbin.getState())
                .attributes(cardbin.getAttributes())
                .build();
    }

    @Override
    public Completable updateCardbinAttribute(Integer cardbinId, Integer attributeId, UpdateAttributeWebRequest updateAttributeWebRequest) {
        return updateCardbinAttributeInRepository(cardbinId,
                toAttribute(attributeRepository.getOne(attributeId), updateAttributeWebRequest));
    }

    private Completable updateCardbinAttributeInRepository(Integer cardbinId, Attribute attribute){
        return Maybe.fromCallable(() -> cardbinRepository.getOne(cardbinId))
                .map(cardbin -> {
                    if (findCardbin(cardbin))
                        return attributeRepository.save(attribute);
                    else
                        return new EntityNotFoundException();
                })
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
    public Completable deleteCardbin(Integer cardbinId) {
        return Maybe.fromCallable(() -> cardbinRepository.getOne(cardbinId))
                .map(cardbin -> {
                    cardbin.setState("INACTIVE");
                    return cardbin;
                })
                .doOnSuccess(cardbin -> cardbinRepository.save(cardbin))
                .ignoreElement();

    }

    @Override
    public Completable deleteCardbinAttribute(Integer cardbinId, Integer attributeId) {
        return Maybe.fromCallable(() -> cardbinRepository.getOne(cardbinId))
                .map(cardbin -> toDeleteCardbinAttributes(cardbin, attributeId))
                .doOnSuccess(cardbin -> cardbinRepository.save(cardbin))
                .ignoreElement();
    }

    private Cardbin toDeleteCardbinAttributes(Cardbin cardbin, Integer attributeId){
        cardbin.setAttributes(cardbin.getAttributes().stream()
        .filter(attribute -> attribute.getId() != attributeId)
        .collect(Collectors.toList()));
        return cardbin;
    }
}
