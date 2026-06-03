package com.visualpathit.account.utilsTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.model.User;
import com.visualpathit.account.utils.MemcachedUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class MemcachedUtilsTest {

    @Before
    public void setup() throws Exception {
        Components components = new Components();
        components.setActiveHost("");
        components.setActivePort("");
        components.setStandByHost("");
        components.setStandByPort("");

        Field f = MemcachedUtils.class.getDeclaredField("object");
        f.setAccessible(true);
        f.set(null, components);
    }

    @Test
    public void shouldReturnCacheUnavailableWhenClientNull() {

        User user = new User();

        String result = MemcachedUtils.memcachedSetData(user, "key");

        assertNotNull(result);
        assertTrue(result.toLowerCase().contains("cache"));
    }

    @Test
    public void shouldReturnNullFromGetWhenNoConnection() {

        User result = MemcachedUtils.memcachedGetData("key");

        assertNull(result);
    }

    @Test
    public void shouldHandleEmptyConfiguration() {

        assertNull(MemcachedUtils.memcachedConnection());
        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldHandleValidMethodFlowWithoutThrowingException() {

        try {
            MemcachedUtils.memcachedSetData(new User(), "test");
            MemcachedUtils.memcachedGetData("test");
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
}