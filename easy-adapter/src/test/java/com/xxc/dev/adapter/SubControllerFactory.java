package com.xxc.dev.adapter;

public interface SubControllerFactory extends MyControllerFactory {

    @ViewType(5)
    DogController createSubFactory();

    @ViewType(11)
    AnimalController<Dog> subController();

}
