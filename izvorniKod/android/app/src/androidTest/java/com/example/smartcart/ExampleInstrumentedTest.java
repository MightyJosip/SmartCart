package com.example.smartcart;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void SmartCartDatabaseTest() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Connector conn = Connector.getInstance(appContext);

        final String[] result = new String[1];

        CountDownLatch lock = new CountDownLatch(1);

        conn.fetchTrgovine(response -> {
            result[0] = response;
        },
            error -> fail());

        lock.await(2000, TimeUnit.MILLISECONDS);

        assertNotNull(result[0]);
    }
}