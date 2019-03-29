package pl.milk.aggregator.service;

public interface AccuWeatherService {
    //using accuweather provider here to get resposne from restapi and manipulate that data for saving it in DB

    //it should be done in parallel (every api-call in different thread)

    //then using weather repository - saving it to db
}
