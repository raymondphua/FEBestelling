package com.infosupport.team2.febestelling.adapter;

import android.content.Context;
import android.test.AndroidTestCase;

import com.infosupport.team2.febestelling.model.Order;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static android.R.attr.order;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by paisanrietbroek on 22/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListOrderAdapterTest {


    private ListOrderAdapter listOrderAdapter;

    @Mock
    Context context;

    @Before
    public void initMock() {
        listOrderAdapter = new ListOrderAdapter(context, 0);
    }

    @Test
    public void filterLogicForFindingOrders() throws Exception {

        Order order = new Order();
        CharSequence constraint;
        boolean match;

        order.setId("2");
        constraint = "2";
        match = listOrderAdapter.isMatch(order, constraint);
        assertThat(match, is(true));


        order.setId("1");
        constraint = "2";
        match = listOrderAdapter.isMatch(order, constraint);
        assertThat(match, is(false));

    }

}