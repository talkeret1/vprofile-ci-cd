package com.visualpathit.account.utilsTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.visualpathit.account.model.User;
import com.visualpathit.account.utils.MemcachedUtils;

public class MemcachedUtilsTest {

    @Test
    public void shouldHandleSetDataWithoutConfiguration() {

        User user = new User();

        String result = MemcachedUtils.memcachedSetData(user, "key");

        assertNotNull(result);
    }

    @Test
    public void shouldReturnNullWhenCacheUnavailable() {

        User result = MemcachedUtils.memcachedGetData("key");

        assertNull(result);
    }

    @Test
    public void shouldHandleConnectionWithoutConfiguration() {

        MemcachedUtils.memcachedConnection();

        MemcachedUtils.standByMemcachedConn();
    }
}