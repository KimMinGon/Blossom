package com.example.kitcoop.lastproject1.pedometerService;

import com.squareup.otto.Bus;

/**
 * Created by kitcoop on 2017-04-04.
 */

public class BusProvider {
    private static final Bus BUS = new Bus();

    private BusProvider() {

    }

    public static Bus getInstance() {
        return BUS;
    }
}
