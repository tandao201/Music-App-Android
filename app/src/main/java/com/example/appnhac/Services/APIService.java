package com.example.appnhac.Services;

public class APIService {

    private static String url="https://musicprojectttcs.000webhostapp.com/Server/";
    public static Dataservice getService(){
        return APIRetrofitClient.getClient(url).create(Dataservice.class);
    }
}
