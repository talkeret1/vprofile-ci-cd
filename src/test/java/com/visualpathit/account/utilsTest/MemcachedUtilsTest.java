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
    public void resetState() throws Exception {
        clearComponents();
    }

    // ---------------------------
    // BASIC NULL OBJECT SCENARIOS
    // ---------------------------

    @Test
    public void memcachedSet_shouldReturnUnavailableWhenObjectNull() {
        String result = MemcachedUtils.memcachedSetData(new User(), "key");
        assertEquals("Cache unavailable", result);
    }

    @Test
    public void memcachedGet_shouldReturnNullWhenObjectNull() {
        assertNull(MemcachedUtils.memcachedGetData("key"));
    }

    @Test
    public void connection_shouldReturnNullWhenObjectNull() {
        assertNull(MemcachedUtils.memcachedConnection());
        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    // ---------------------------
    // ACTIVE CONFIG VALIDATION
    // ---------------------------

    @Test
    public void shouldReturnNullWhenActiveConfigEmpty() throws Exception {

        Components c = new Components();
        c.setActiveHost("");
        c.setActivePort("");
        c.setStandByHost("");
        c.setStandByPort("");

        setComponents(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }

    @Test
    public void shouldReturnNullWhenActiveConfigInvalidHostPort() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("invalid");
        c.setStandByPort("9999");

        setComponents(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }

    // ---------------------------
    // STANDBY BRANCH COVERAGE
    // ---------------------------

    @Test
    public void shouldReturnNullWhenStandbyHostMissing() throws Exception {

        Components c = new Components();
        c.setStandByHost(null);
        c.setStandByPort("11211");

        setComponents(c);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldReturnNullWhenStandbyPortMissing() throws Exception {

        Components c = new Components();
        c.setStandByHost("localhost");
        c.setStandByPort("");

        setComponents(c);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldReturnNullWhenStandbyConfigInvalid() throws Exception {

        Components c = new Components();
        c.setStandByHost("invalid");
        c.setStandByPort("9999");

        setComponents(c);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    // ---------------------------
    // SAFE EXECUTION PATHS
    // ---------------------------

    @Test
    public void shouldHandleSetDataSafelyWithInvalidConnection() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("invalid");
        c.setStandByPort("9999");

        setComponents(c);

        String result = MemcachedUtils.memcachedSetData(new User(), "key");

        assertNotNull(result);
        assertTrue(result.startsWith("Cache"));
    }

    @Test
    public void shouldHandleGetDataSafelyWithInvalidConnection() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("invalid");
        c.setStandByPort("9999");

        setComponents(c);

        User result = MemcachedUtils.memcachedGetData("key");

        assertNull(result);
    }

    // ---------------------------
    // EDGE FALLBACK COVERAGE
    // ---------------------------

    @Test
    public void shouldFallbackToStandbyLogicWithoutThrowing() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("localhost");
        c.setStandByPort("11211");

        setComponents(c);

        try {
            MemcachedUtils.memcachedConnection();
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
}