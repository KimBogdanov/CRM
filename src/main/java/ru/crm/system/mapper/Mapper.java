package ru.crm.system.mapper;

public interface Mapper<F, T> {

    T map(F object);

}