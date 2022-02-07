/**
 * Copyright (C) 2020 Vaadin Ltd
 *
 * This program is available under Commercial Vaadin Developer License
 * 4.0 (CVDLv4).
 *
 *
 * For the full License, see <https://vaadin.com/license/cvdl-4.0>.
 */
package com.vaadin.testbench.screenshot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;

public class ReferenceNameGeneratorTest {

    private ReferenceNameGenerator rng;

    @Before
    public void setUp() {
        rng = new ReferenceNameGenerator();
    }

    @Test
    public void testCreateReferenceNameGenerator() {
        assertNotNull(rng);
    }

    @Test
    public void testGenerateName_shotFirefox11inCapabilities_returnsGeneratedName() {
        Capabilities ffcaps = Mockito.mock(Capabilities.class);
        Mockito.when(ffcaps.getPlatform()).thenReturn(Platform.XP);
        Mockito.when(ffcaps.getBrowserName()).thenReturn("Firefox");
        Mockito.when(ffcaps.getVersion()).thenReturn("13.0.1");
        String name = rng.generateName("shot", ffcaps);
        assertEquals("shot_xp_Firefox_13", name);
    }

    @Test
    public void testGenerateName_shotNoPlatformInCapabilities_returnsGeneratedName() {
        Capabilities someBrowser = Mockito.mock(Capabilities.class);
        Mockito.when(someBrowser.getBrowserName()).thenReturn("SomeBrowser");
        Mockito.when(someBrowser.getVersion()).thenReturn("12.3");
        String name = rng.generateName("shot", someBrowser);
        assertEquals("shot_unknown_SomeBrowser_12", name);
    }

    @Test
    public void testGenerateName_fooSafari5inCapabilities_returnsGeneratedName() {
        Capabilities safari = Mockito.mock(Capabilities.class);
        Mockito.when(safari.getPlatform()).thenReturn(Platform.MAC);
        Mockito.when(safari.getBrowserName()).thenReturn("Safari");
        Mockito.when(safari.getVersion()).thenReturn("5");
        String name = rng.generateName("foo", safari);
        assertEquals("foo_mac_Safari_5", name);
    }

    @Test
    public void testGenerateName_shotEdgeinCapabilities_returnsGeneratedName() {
        Capabilities chrome = Mockito.mock(Capabilities.class);
        Mockito.when(chrome.getPlatform()).thenReturn(Platform.XP);
        Mockito.when(chrome.getBrowserName()).thenReturn("MicrosoftEdge");
        Mockito.when(chrome.getVersion()).thenReturn("");
        Mockito.when(chrome.getCapability("browserVersion")).thenReturn("25");
        String name = rng.generateName("shot", chrome);
        assertEquals("shot_xp_MicrosoftEdge_25", name);
    }
}