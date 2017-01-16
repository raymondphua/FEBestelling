package com.infosupport.team2.febestelling.model;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public enum Status {

    BESTELD, AFGELEVERD;

    @Override
    public String toString() {
        switch(this) {
            case BESTELD: return "Besteld";
            case AFGELEVERD: return "Afgeleverd";
            default: return "Onbekend status";
        }
    }
}
