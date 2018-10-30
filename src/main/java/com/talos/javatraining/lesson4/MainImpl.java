package com.talos.javatraining.lesson4;


import com.talos.javatraining.lesson4.exceptions.AddressNotFoundException;
import com.talos.javatraining.lesson4.model.AddressModel;
import com.talos.javatraining.lesson4.model.CountryModel;
import com.talos.javatraining.lesson4.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;


public class MainImpl implements Main {

    public static final String CA = "CA";
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final String MESSAGE = "the user has not been logged yet";

    @Override
    public String getLine1(AddressModel addressModel) {
        return Optional.ofNullable(addressModel)
                .map(AddressModel::getLine1)
                .filter(StringUtils::isNotBlank)
                .orElse(StringUtils.EMPTY);
    }

    @Override
    public String getFullName(AddressModel addressModel) {
        return Stream.of(Optional.ofNullable(addressModel)
                        .map(AddressModel::getFirstName)
                        .filter(StringUtils::isNotBlank)
                        .orElse(StringUtils.EMPTY),
                Optional.ofNullable(addressModel).
                        map(AddressModel::getLastName)
                        .filter(StringUtils::isNotBlank)
                        .orElse(StringUtils.EMPTY))
                .collect(Collectors.joining(" "))
                .trim();
    }

    @Override
    public AddressModel getBillingAddress(UserModel userModel) {
        return Optional.ofNullable(userModel)
                .map(UserModel::getAddresses)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(this::isBillingAddress)
                .findAny()
                .orElse(null);
    }

    public boolean isBillingAddress(AddressModel addressModel){
        return BooleanUtils.isTrue(addressModel.getBillingAddress());
    }


    @Override
    public String getLastLoginFormatted(UserModel userModel) {
        DateFormat format = new SimpleDateFormat(MM_DD_YYYY);
        return Optional.ofNullable(userModel)
                .map(UserModel::getLastLogin)
                .map(date -> format.format(date))
                .orElse(MESSAGE);

    }

    @Override
    public String getContactCountry(UserModel userModel) {
        return Optional.ofNullable(userModel)
                .map(UserModel::getAddresses)
                .orElseGet( ArrayList::new)
                .stream()
                .filter(this::isContactAddress)
                .findAny()
                .map(AddressModel::getCountry)
                .map(CountryModel::getIsocode)
                .orElse(CA);
    }

    public boolean isContactAddress(AddressModel addressModel){
        return BooleanUtils.isTrue(addressModel.getContactAddress());
    }

    @Override
    public AddressModel getShippingAddress(UserModel userModel) throws AddressNotFoundException {
        return Optional.ofNullable(userModel)
                .map(UserModel::getAddresses)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(this::isShippingAddress)
                .findAny()
                .orElseThrow(AddressNotFoundException::new);
    }

    public boolean isShippingAddress(AddressModel addressModel){
        return BooleanUtils.isTrue(addressModel.getShippingAddress());
    }

    public boolean isAddress(Supplier<Boolean> supplier){
        return BooleanUtils.isTrue(supplier.get());

    }


    // ----------------------------------
    // DON'T MODIFY THE FOLLOWING METHODS
    // ----------------------------------

    /**
     * This method returns an address based on the condition
     *
     * @param addresses the address list
     * @param condition the condition
     * @return the first address that matches the condition
     */
    private AddressModel getAddress(Collection<AddressModel> addresses, Predicate<AddressModel> condition) {
        for (AddressModel addressModel : addresses) {
            if (condition.test(addressModel)) {
                return addressModel;
            }
        }
        return null;
    }

    /**
     * This method takes 1 second to return a response
     *
     * @return the user country
     */
    private String inferCountry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {

        }
        return "CA";
    }
}
