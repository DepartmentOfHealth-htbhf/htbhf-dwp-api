package uk.gov.dhsc.htbhf.dwp.entity;

import java.util.Set;

public interface Household {

    String getHouseholdIdentifier();

    Set<? extends Child> getChildren();
}
