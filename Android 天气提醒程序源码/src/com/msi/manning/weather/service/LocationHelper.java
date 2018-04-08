package com.msi.manning.weather.service;

import android.location.Criteria;

public class LocationHelper {

    private static final String CLASSTAG = LocationHelper.class.getSimpleName();

    public static final Criteria PROVIDER_CRITERIA;

    static {
        PROVIDER_CRITERIA = new Criteria();
        LocationHelper.PROVIDER_CRITERIA.setAccuracy(Criteria.NO_REQUIREMENT);
        LocationHelper.PROVIDER_CRITERIA.setAltitudeRequired(false);
        LocationHelper.PROVIDER_CRITERIA.setBearingRequired(false);
        LocationHelper.PROVIDER_CRITERIA.setCostAllowed(false);
        LocationHelper.PROVIDER_CRITERIA.setPowerRequirement(Criteria.NO_REQUIREMENT);
        LocationHelper.PROVIDER_CRITERIA.setSpeedRequired(false);
    }

}
