package com.visualpathit.account.utilsTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.model.User;
import com.visualpathit.account.utils.MemcachedUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class MemcachedUtilsTest {

    private void setComponents(Components components) throws Exception {
        Field f = MemcachedUtils.class.getDeclaredField("object");
        f.setAccessible(true);
        f.set(null, components);
    }

    private void clearComponents() throws Exception {
        Field f = MemcachedUtils.class.getDeclaredField("object");
        f.setAccessible(true);
        f.set(null, null);
    }

    @Before
    public void reset() throws Exception {
        clearComponents();
    }

    @Test
    public void shouldReturnCacheUnavailableWhenObjectIsNull() {
        String result = MemcachedUtils.memcachedSetData(new User(), "key");
        assertEquals("Cache unavailable", result);
    }

    @Test
    public void shouldReturnNullFromGetWhenObjectIsNull() {
        User result = MemcachedUtils.memcachedGetData("key");
        assertNull(result);
    }

    @Test
    public void shouldReturnNullConnectionWhenObjectIsNull() {
        assertNull(MemcachedUtils.memcachedConnection());
        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldHandleInvalidConfigGracefully() throws Exception {

        Components components = new Components();
        components.setActiveHost("");
        components.setActivePort("");
        components.setStandByHost("");
        components.setStandByPort("");

        setComponents(components);

        assertNull(MemcachedUtils.memcachedConnection());
        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldHandlePartialStandbyConfig() throws Exception {

        Components components = new Components();
        components.setStandByHost("localhost");
        components.setStandByPort("");

        setComponents(components);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldHandleNullStandbyHost() throws Exception {

        Components components = new Components();
        components.setStandByHost(null);
        components.setStandByPort("11211");

        setComponents(components);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldHandleInterruptedExceptionFlowSafely() throws Exception {
        // לא באמת נכנס ל-InterruptedException בלי mocking
        // אבל מכסה flow מלא של setData עם config null/invalid
        Components components = new Components();
        components.setActiveHost("invalid");
        components.setActivePort("99999");
        setComponents(components);

        String result = MemcachedUtils.memcachedSetData(new User(), "key");
        assertTrue(result.startsWith("Cache set failed") || result.equals("Cache unavailable"));
    }

    @Test
    public void shouldHandleGetDataFailureGracefully() throws Exception {

        Components components = new Components();
        components.setActiveHost("invalid");
        components.setActivePort("99999");
        setComponents(components);

        User result = MemcachedUtils.memcachedGetData("key");
        assertNull(result);
    }
}