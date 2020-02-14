package com.example.one.service;

import com.example.one.webdto.request.AddCardbinWebRequest;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface AttributeService {

    //CREATE
    Single addAttribute(AddCardbinWebRequest addCardbinWebRequest);
    //RETRIEVE
    Observable getAllAttributes();



}
