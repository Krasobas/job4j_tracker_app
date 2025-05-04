package ru.job4j.tracker.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryAddressMapper {
    @Mapping(source = "student.name", target = "name")
    @Mapping(source = "address.houseNo", target = "houseNumber")
    DeliveryAddressDTO getDeliveryAddress(StudentEntity student, AddressEntity address);
}
