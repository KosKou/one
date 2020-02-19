package com.example.one.web;

import org.junit.Test;

import com.example.one.entity.Attribute;
import com.example.one.entity.CardBin;
import com.example.one.service.cardbin.CardbinServiceImpl;
import com.example.one.servicedto.CardbinResponse;
import com.example.one.webdto.request.AddAttributeWebRequest;
import com.example.one.webdto.request.AddCardbinWebRequest;
import com.example.one.webdto.request.UpdateAttributeWebRequest;
import com.example.one.webdto.request.UpdateCardbinWebRequest;
import com.example.one.webdto.response.BaseWebResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class CardBinControllerTest {

    @InjectMocks
    private CardBinController controller;

    @Mock
    private CardbinServiceImpl cardbinService;

    @Mock
    private CardBin cardbin;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        this.initCardbin();
    }

    public void initCardbin(){
        cardbin = new CardBin();
        cardbin.setId(1);
        cardbin.setBinNumber(1210);
        cardbin.setBinType("TEST 1");
        cardbin.setState("ACTIVE");
    }

    @Test
    public void createCardBin() {
        Mockito.when(cardbinService.addCardbin(Mockito.any()))
                .thenReturn(Single.just(Mockito.mock(CardBin.class)));
        TestObserver<BaseWebResponse<?>> testObserver = controller.createCardBin(AddCardbinWebRequest.builder()
                .binNumber(1210)
                .binType("TEST 1")
                .build()).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

    @Test
    public void createCardbinAttribute() {
        Mockito.when(cardbinService.addCardbinAttribute(Mockito.anyInt(), Mockito.any()))
                .thenReturn(Single.just(Mockito.mock(CardBin.class)));
        TestObserver<BaseWebResponse<?>> testObserver = controller
                .createCardbinAttribute(AddAttributeWebRequest.builder()
                        .key("1010")
                        .value("TESTING")
                        .build(), 1).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();

    }

    @Test
    public void retrieveAllCardbins() {
        List<CardbinResponse> cardbinList = new ArrayList<>();
        cardbinList.add(CardbinResponse.builder().build());

        Mockito.when(cardbinService.retrieveAllCardbins())
                .thenReturn(Observable.fromIterable(cardbinList));

        TestObserver<BaseWebResponse<CardbinResponse>> testObserver = controller
                .retrieveAllCardbins().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

    @Test
    public void retrieveCardbin() {
        Mockito.when(cardbinService.retrieveCardbin(Mockito.anyInt()))
                .thenReturn(Single.just(Mockito.mock(CardbinResponse.class)));

        TestObserver<BaseWebResponse<CardbinResponse>> testObserver = controller.retrieveCardbin(1).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

    @Test
    public void retrieveCardbinAttributes() {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(Attribute.builder().build());
        Mockito.when(cardbinService
                .retrieveAllCardbinAttributes(Mockito.anyInt()))
                .thenReturn(Observable.fromIterable(attributeList));
        TestObserver<BaseWebResponse<Attribute>> testObserver = controller
                .retrieveCardbinAttributes(1).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();

    }

    @Test
    public void retrieveCardbinAttribute() {
        Mockito.when(cardbinService
                .retrieveCardbinAttribute(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Single.just(Mockito.mock(Attribute.class)));
        TestObserver<BaseWebResponse<Attribute>> testObserver = controller
                .retrieveCardbinAttribute(1,1).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

    @Test
    public void updateCardbin() {
        Completable testing = Completable.complete();
        testing.subscribeOn(Schedulers.io())
                .toSingle(BaseWebResponse::successNoData);
        Mockito.when(cardbinService.updateCardBin(Mockito.anyInt(),
                Mockito.any()))
                .thenReturn(testing);
        TestObserver<BaseWebResponse<?>> testObserver = controller
                .updateCardbin(1,
                        UpdateCardbinWebRequest.builder().build()).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

    @Test
    public void testUpdateCardbinAttribute() {
        Completable testing = Completable.complete();
        testing.subscribeOn(Schedulers.io())
                .toSingle(BaseWebResponse::successNoData);
        Mockito.when(cardbinService
                .updateCardBinAttribute(Mockito.anyInt(), Mockito.anyInt(),Mockito.any()))
                .thenReturn(testing);
        TestObserver<BaseWebResponse<?>> testObserver = controller.updateCardbinAttribute(1,1
                , UpdateAttributeWebRequest.builder().build()).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

    @Test
    public void deleteCardbin() {
        Completable testing = Completable.complete();
        testing.subscribeOn(Schedulers.io())
                .toSingle(BaseWebResponse::successNoData);
        Mockito.when(cardbinService.deleteCardBin(Mockito.anyInt()))
                .thenReturn(testing);
        TestObserver<BaseWebResponse<?>> testObserver = controller.deleteCardbin(1).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

    @Test
    public void deleteCardbinAttribute() {
        Completable testing = Completable.complete();
        testing.subscribeOn(Schedulers.io())
                .toSingle(BaseWebResponse::successNoData);
        Mockito.when(cardbinService.deleteCardBinAttribute(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(testing);
        TestObserver<BaseWebResponse<?>> testObserver = controller.deleteCardbinAttribute(1,2).test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }
}