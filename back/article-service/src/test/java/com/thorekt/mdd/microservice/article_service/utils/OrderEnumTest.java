package com.thorekt.mdd.microservice.article_service.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import utils.OrderEnum;

@ExtendWith(MockitoExtension.class)
public class OrderEnumTest {

    @Test
    public void testIsAsc() {
        assert (OrderEnum.ASC.isAsc());
        assert (!OrderEnum.DESC.isAsc());
    }

    @Test
    public void testIsDesc() {
        assert (OrderEnum.DESC.isDesc());
        assert (!OrderEnum.ASC.isDesc());
    }
}
