/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java.topology;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;

import de.ailis.usb4java.descriptors.SimpleUsbInterfaceDescriptor;
import de.ailis.usb4java.libusb.EndpointDescriptor;
import de.ailis.usb4java.libusb.InterfaceDescriptor;

/**
 * usb4java implementation of UsbInterface.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb4JavaInterface implements UsbInterface
{
    /** The configuration this interface belongs to. */
    private final Usb4JavaConfiguration configuration;

    /** The interface descriptor. */
    private final UsbInterfaceDescriptor descriptor;

    /** The endpoints of this interface. */
    private final Map<Byte, Usb4JavaEndpoint> endpoints =
        new HashMap<Byte, Usb4JavaEndpoint>();

    /**
     * Constructor.
     * 
     * @param configuration
     *            The USB configuration this interface belongs to.
     * @param descriptor
     *            The libusb interface descriptor.
     */
    Usb4JavaInterface(final Usb4JavaConfiguration configuration,
        final InterfaceDescriptor descriptor)
    {
        this.configuration = configuration;
        this.descriptor = new SimpleUsbInterfaceDescriptor(descriptor);
        for (EndpointDescriptor endpointDescriptor: descriptor.endpoint())
        {
            final Usb4JavaEndpoint endpoint =
                new Usb4JavaEndpoint(this, endpointDescriptor);
            this.endpoints.put(endpointDescriptor.bEndpointAddress(), endpoint);
        }
    }


    /**
     * Ensures this setting and configuration is active.
     *
     * @throws UsbNotActiveException
     *             When the setting or the configuration is not active.
     */
    private void checkActive()
    {
        if (!this.configuration.isActive())
            throw new UsbNotActiveException("Configuration is not active");
        if (!isActive())
            throw new UsbNotActiveException("Setting is not active");
    }

    /**
     * Ensures that the device is connected.
     *
     * @throws UsbDisconnectedException
     *             When device has been disconnected.
     */
    private void checkConnected()
    {
        this.configuration.getUsbDevice().checkConnected();
    }

    @Override
    public void claim() throws UsbException
    {
        claim(null);
    }

    @Override
    public void claim(final UsbInterfacePolicy policy) throws UsbException
    {
        checkActive();
        checkConnected();
        final Usb4JavaDevice device = this.configuration.getUsbDevice();
        device.claimInterface(this.descriptor.bInterfaceNumber(),
            policy != null && policy.forceClaim(this));
        this.configuration.setUsbInterface(
            this.descriptor.bInterfaceNumber(), this);
    }

    @Override
    public void release() throws UsbException
    {
        checkActive();
        checkConnected();
        this.configuration.getUsbDevice().releaseInterface(
            this.descriptor.bInterfaceNumber());
    }

    @Override
    public boolean isClaimed()
    {
        return this.configuration.getUsbDevice().isInterfaceClaimed(
            this.descriptor.bInterfaceNumber());
    }

    @Override
    public boolean isActive()
    {
        return this.configuration.getUsbInterface(this.descriptor
            .bInterfaceNumber()) == this;
    }

    @Override
    public int getNumSettings()
    {
        return this.configuration.getNumSettings(this.descriptor
            .bInterfaceNumber());
    }

    @Override
    public byte getActiveSettingNumber()
    {
        checkActive();
        return this.configuration
            .getUsbInterface(this.descriptor.bInterfaceNumber())
            .getUsbInterfaceDescriptor().bAlternateSetting();
    }

    @Override
    public Usb4JavaInterface getActiveSetting()
    {
        checkActive();
        return this.configuration.getUsbInterface(this.descriptor
                .bInterfaceNumber());
    }

    @Override
    public Usb4JavaInterface getSetting(final byte number)
    {
        return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).get(number & 0xff);
    }

    @Override
    public boolean containsSetting(final byte number)
    {
        return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).containsKey(number & 0xff);
    }

    @Override
    public List<Usb4JavaInterface> getSettings()
    {
        return Collections.unmodifiableList(new ArrayList<Usb4JavaInterface>(
            this.configuration.getSettings(
                this.descriptor.bInterfaceNumber()).values()));
    }

    @Override
    public List<Usb4JavaEndpoint> getUsbEndpoints()
    {
        return Collections.unmodifiableList(new ArrayList<Usb4JavaEndpoint>(
            this.endpoints.values()));
    }

    @Override
    public Usb4JavaEndpoint getUsbEndpoint(final byte address)
    {
        return this.endpoints.get(address);
    }

    @Override
    public boolean containsUsbEndpoint(final byte address)
    {
        return this.endpoints.containsKey(address);
    }

    @Override
    public Usb4JavaConfiguration getUsbConfiguration()
    {
        return this.configuration;
    }

    @Override
    public UsbInterfaceDescriptor getUsbInterfaceDescriptor()
    {
        return this.descriptor;
    }

    @Override
    public String getInterfaceString() throws UsbException,
        UnsupportedEncodingException
    {
        checkConnected();
        final byte iInterface = this.descriptor.iInterface();
        if (iInterface == 0) return null;
        return this.configuration.getUsbDevice().getString(iInterface);
    }
        
    @Override
    public String toString()
    {
        return String.format("USB interface %02x", 
            this.descriptor.bInterfaceNumber());
    }    
}
