package com.visualpathit.account.utilsTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.model.User;
import com.visualpathit.account.utils.MemcachedUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class MemcachedUtilsTest {

    private void setComponentsField(Components components) throws Exception {
        Field f = MemcachedUtils.class.getDeclaredField("object");
        f.setAccessible(true);
        f.set(null, components);
    }

    private Components getComponentsField() throws Exception {
        Field f = MemcachedUtils.class.getDeclaredField("object");
        f.setAccessible(true);
        return (Components) f.get(null);
    }

    private void clearComponents() throws Exception {
        setComponentsField(null);
    }

    @Before
    public void resetState() throws Exception {
        clearComponents();
    }

    // =========================================================
    // constructor + spring setter coverage
    // =========================================================

    @Test
    public void constructor_shouldCreateInstance() {
        MemcachedUtils utils = new MemcachedUtils();
        assertNotNull(utils);
    }

    @Test
    public void setComponents_shouldAssignStaticField() throws Exception {

        Components components = new Components();

        MemcachedUtils utils = new MemcachedUtils();
        utils.setComponents(components);

        assertSame(components, getComponentsField());
    }

    // =========================================================
    // NULL OBJECT
    // =========================================================

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
    }

    @Test
    public void standbyConnection_shouldReturnNullWhenObjectNull() {
        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    // =========================================================
    // ACTIVE CONFIG
    // =========================================================

    @Test
    public void shouldReturnNullWhenActiveConfigEmpty() throws Exception {

        Components c = new Components();
        c.setActiveHost("");
        c.setActivePort("");
        c.setStandByHost("");
        c.setStandByPort("");

        setComponentsField(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }

    @Test
    public void shouldReturnNullWhenActiveHostNull() throws Exception {

        Components c = new Components();
        c.setActiveHost(null);
        c.setActivePort("11211");

        setComponentsField(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }

    @Test
    public void shouldReturnNullWhenActivePortNull() throws Exception {

        Components c = new Components();
        c.setActiveHost("localhost");
        c.setActivePort(null);

        setComponentsField(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }

    @Test
    public void shouldReturnNullWhenActiveConfigInvalidHostPort() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("invalid");
        c.setStandByPort("9999");

        setComponentsField(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }

    @Test
    public void shouldHandleInvalidPortFormat() throws Exception {

        Components c = new Components();
        c.setActiveHost("localhost");
        c.setActivePort("ABC");

        setComponentsField(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }

    // =========================================================
    // STANDBY CONFIG
    // =========================================================

    @Test
    public void shouldReturnNullWhenStandbyHostMissing() throws Exception {

        Components c = new Components();
        c.setStandByHost(null);
        c.setStandByPort("11211");

        setComponentsField(c);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldReturnNullWhenStandbyPortMissing() throws Exception {

        Components c = new Components();
        c.setStandByHost("localhost");
        c.setStandByPort("");

        setComponentsField(c);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldReturnNullWhenStandbyConfigInvalid() throws Exception {

        Components c = new Components();
        c.setStandByHost("invalid");
        c.setStandByPort("9999");

        setComponentsField(c);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    @Test
    public void shouldReturnNullWhenStandbyPortNotNumeric() throws Exception {

        Components c = new Components();
        c.setStandByHost("localhost");
        c.setStandByPort("ABC");

        setComponentsField(c);

        assertNull(MemcachedUtils.standByMemcachedConn());
    }

    // =========================================================
    // SET / GET
    // =========================================================

    @Test
    public void shouldHandleSetDataSafelyWithInvalidConnection() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("invalid");
        c.setStandByPort("9999");

        setComponentsField(c);

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

        setComponentsField(c);

        User result = MemcachedUtils.memcachedGetData("key");

        assertNull(result);
    }

    @Test
    public void shouldHandleNullKeyInGet() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");

        setComponentsField(c);

        assertNull(MemcachedUtils.memcachedGetData(null));
    }

    @Test
    public void shouldHandleNullKeyInSet() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");

        setComponentsField(c);

        String result = MemcachedUtils.memcachedSetData(new User(), null);

        assertNotNull(result);
    }

    // =========================================================
    // FALLBACK
    // =========================================================

    @Test
    public void shouldFallbackToStandbyLogicWithoutThrowing() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("localhost");
        c.setStandByPort("11211");

        setComponentsField(c);

        try {
            MemcachedUtils.memcachedConnection();
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void shouldHandleEmptyStandbyConfiguration() throws Exception {

        Components c = new Components();
        c.setActiveHost("invalid");
        c.setActivePort("9999");
        c.setStandByHost("");
        c.setStandByPort("");

        setComponentsField(c);

        assertNull(MemcachedUtils.memcachedConnection());
    }
}