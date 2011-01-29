/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.usb4java;

import java.nio.ByteBuffer;


/**
 * The interface descriptor describes a specific interface of a USB
 * configuration.
 *
 * @author Klaus Reimer (k@ailis.de)
 */

public final class USB_Interface_Descriptor extends USB_Descriptor_Header
{
    /**
     * Constructor.
     *
     * @param data
     *            The descriptor data
     */

    public USB_Interface_Descriptor(final ByteBuffer data)
    {
        super(data);
    }


    /**
     * Returns the zero-based interface number.
     *
     * @return The interface number (unsigned byte).
     */

    public native int bInterfaceNumber();


    /**
     * Returns the value used to select this alternate setting for the
     * interface.
     *
     * @return The value used to select this alternate setting (unsigned byte).
     */

    public native int bAlternateSetting();


    /**
     * Returns the number of endpoints.
     *
     * @return The number of endpoints (unsigned byte).
     */

    public native int bNumEndpoints();


    /**
     * Returns the interface class code as assigned by the USB-IF. Class 0 is
     * reserved for future standardization. Class 0xff means that the interface
     * class is vendor-specific. All other values are reserved for assignment by
     * the USB_IF.
     *
     * @return The interface class code (unsigned byte).
     */

    public native int bInterfaceClass();


    /**
     * Returns the interface sub class code as assigned by the USB-IF. These
     * codes are qualified by the interface class. If bInterfaceClass is 0 then
     * the sub class is also 0. If class is not 0xff then all sub classes are
     * reserved by the USB-IF.
     *
     * @return The interface sub class code (unsigned byte).
     */

    public native int bInterfaceSubClass();


    /**
     * Returns the protocol code as assigned by the USB-IF. These codes are
     * qualified by the interface class and sub class. If an interface supports
     * class-specific requests, this code identifies the protocols that the
     * device uses as defined by the specification of the device class. If this
     * field is set to zero then the device does not use a class-specific
     * protocol on this interface. If this field is set to 0xff then the device
     * uses a vendor-specific protocol for this interface.
     *
     * @return The protocol code (unsigned byte).
     */

    public native int bInterfaceProtocol();


    /**
     * Returns the index of the string descriptor describing this interface.
     *
     * @return The string descriptor index (unsigned byte).
     */

    public native int iInterface();


    /**
     * Returns the array with endpoints.
     *
     * @return The array with endpoints.
     */

    public native USB_Endpoint_Descriptor[] endpoint();


    /**
     * Returns the extra descriptor data.
     *
     * @return The extra descriptor data.
     */

    public native ByteBuffer extra();


    /**
     * Returns the size of the extra data in bytes.
     *
     * @return The size of the extra data in bytes.
     */

    public native int extralen();
}