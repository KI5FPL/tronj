package com.github.ki5fpl.tronj.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ki5fpl.tronj.client.TronClient;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.tron.api.GrpcAPI.BlockExtention;
import org.tron.api.GrpcAPI.EmptyMessage;

public class ClientTest {
    @Test
    public void testGetNowBlockQuery() {
        TronClient client = TronClient.ofShasta("3333333333333333333333333333333333333333333333333333333333333333");
        BlockExtention block = client.blockingStub.getNowBlock2(EmptyMessage.newBuilder().build());

        System.out.println(block.getBlockHeader());
        assertTrue(block.getBlockHeader().getRawDataOrBuilder().getNumber() > 0);
    }
}
