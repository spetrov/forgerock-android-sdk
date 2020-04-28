/*
 * Copyright (c) 2020 ForgeRock. All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package org.forgerock.android.authenticator;

import android.util.Base64;

import org.forgerock.android.authenticator.exception.MechanismParsingException;

import java.util.Map;

/**
 * Provides the ability to parse URI scheme into a convenient format
 * to use with configuring a {@link Push} to receive push notifications.
 */
class PushParser extends MechanismParser {

    /** The endpoint used for registration */
    public static final String REGISTRATION_ENDPOINT = "r";
    /** The endpoint used for authentication */
    public static final String AUTHENTICATION_ENDPOINT = "a";
    /** The message id to use for response */
    public static final String MESSAGE_ID = "m";
    /** The shared secret used for signing */
    public static final String SHARED_SECRET = "s";
    /** The challenge to use for the response */
    public static final String CHALLENGE = "c";
    /** The challenge to use for the response */
    public static final String AM_LOAD_BALANCER_COOKIE = "l";

    private static final String BASE_64_URL_SHARED_SECRET = "s";
    private static final String BASE_64_URL_CHALLENGE = "c";
    private static final String BASE_64_URL_IMAGE = "image";
    private static final String BASE_64_URL_REG_ENDPOINT = "r";
    private static final String BASE_64_URL_AUTH_ENDPOINT = "a";
    private static final String BASE_64_AM_LOAD_BALANCER_COOKIE_KEY = "l";

    @Override
    protected Map<String, String> postProcess(Map<String, String> values) throws MechanismParsingException {

        if (!containsNonEmpty(values, MESSAGE_ID)) {
            throw new MechanismParsingException("Message ID is required");
        }

        if (containsNonEmpty(values, BASE_64_URL_IMAGE)) {
            byte[] imageBytes = Base64.decode(values.get(BASE_64_URL_IMAGE), Base64.NO_WRAP);
            if (imageBytes != null) {
                values.put(IMAGE, new String(imageBytes));
            }
        }

        if (containsNonEmpty(values, BASE_64_AM_LOAD_BALANCER_COOKIE_KEY)) {
            values.put(AM_LOAD_BALANCER_COOKIE, recodeBase64UrlValueToStringWithValidation(values, BASE_64_AM_LOAD_BALANCER_COOKIE_KEY));
        }

        values.put(REGISTRATION_ENDPOINT, recodeBase64UrlValueToStringWithValidation(values, BASE_64_URL_REG_ENDPOINT));

        values.put(ISSUER, recodeBase64UrlValueToStringWithValidation(values, ISSUER));
        values.put(AUTHENTICATION_ENDPOINT, recodeBase64UrlValueToStringWithValidation(values, BASE_64_URL_AUTH_ENDPOINT));
        values.put(SHARED_SECRET, recodeBase64UrlValueToBase64WithValidation(values, BASE_64_URL_SHARED_SECRET));
        values.put(CHALLENGE, recodeBase64UrlValueToBase64WithValidation(values, BASE_64_URL_CHALLENGE));

        return values;
    }

    byte[] decodeValueWithValidation(Map<String, String> data, String key) throws MechanismParsingException{
        if (!containsNonEmpty(data, key)) {
            throw new MechanismParsingException(key + " must not be empty");
        }
        byte[] bytes = Base64.decode(data.get(key), Base64.NO_WRAP);

        if (bytes == null) {
            throw new MechanismParsingException("Failed to decode value in " + key);
        }
        return bytes;
    }

    String recodeBase64UrlValueToBase64WithValidation(Map<String, String> data, String key) throws MechanismParsingException{
        return Base64.encodeToString(decodeValueWithValidation(data, key), Base64.NO_WRAP);
    }

    String recodeBase64UrlValueToStringWithValidation(Map<String, String> data, String key) throws MechanismParsingException{
        return new String(decodeValueWithValidation(data, key));
    }

}
