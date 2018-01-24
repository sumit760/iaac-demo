package com.comcast.test.citf.common.cima.satrXmlSuccessType2;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceToken" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AccessToken" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
 *                             &lt;element name="token_secret" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ExpiresOn" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Status" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="Code" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "serviceToken",
    "status"
})
@XmlRootElement(name = "AccessTokenResponse")
public class AccessTokenResponse {

    @XmlElement(name = "ServiceToken")
    protected AccessTokenResponse.ServiceToken serviceToken;
    @XmlElement(name = "Status")
    protected AccessTokenResponse.Status status;
    @XmlAttribute(name = "version", required = true)
    protected BigDecimal version;

    /**
     * Gets the value of the serviceToken property.
     * 
     * @return
     *     possible object is
     *     {@link AccessTokenResponse.ServiceToken }
     *     
     */
    public AccessTokenResponse.ServiceToken getServiceToken() {
        return serviceToken;
    }

    /**
     * Sets the value of the serviceToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessTokenResponse.ServiceToken }
     *     
     */
    public void setServiceToken(AccessTokenResponse.ServiceToken value) {
        this.serviceToken = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link AccessTokenResponse.Status }
     *     
     */
    public AccessTokenResponse.Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessTokenResponse.Status }
     *     
     */
    public void setStatus(AccessTokenResponse.Status value) {
        this.status = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="AccessToken" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
     *                   &lt;element name="token_secret" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="ExpiresOn" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "accessToken"
    })
    public static class ServiceToken {

        @XmlElement(name = "AccessToken")
        protected AccessTokenResponse.ServiceToken.AccessToken accessToken;
        @XmlAttribute(name = "ExpiresOn", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String expiresOn;

        /**
         * Gets the value of the accessToken property.
         * 
         * @return
         *     possible object is
         *     {@link AccessTokenResponse.ServiceToken.AccessToken }
         *     
         */
        public AccessTokenResponse.ServiceToken.AccessToken getAccessToken() {
            return accessToken;
        }

        /**
         * Sets the value of the accessToken property.
         * 
         * @param value
         *     allowed object is
         *     {@link AccessTokenResponse.ServiceToken.AccessToken }
         *     
         */
        public void setAccessToken(AccessTokenResponse.ServiceToken.AccessToken value) {
            this.accessToken = value;
        }

        /**
         * Gets the value of the expiresOn property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExpiresOn() {
            return expiresOn;
        }

        /**
         * Sets the value of the expiresOn property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExpiresOn(String value) {
            this.expiresOn = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
         *         &lt;element name="token_secret" type="{http://www.w3.org/2001/XMLSchema}normalizedString" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "token",
            "tokenSecret"
        })
        public static class AccessToken {

            @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
            @XmlSchemaType(name = "normalizedString")
            protected String token;
            @XmlElement(name = "token_secret")
            @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
            @XmlSchemaType(name = "normalizedString")
            protected String tokenSecret;

            /**
             * Gets the value of the token property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getToken() {
                return token;
            }

            /**
             * Sets the value of the token property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setToken(String value) {
                this.token = value;
            }

            /**
             * Gets the value of the tokenSecret property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTokenSecret() {
                return tokenSecret;
            }

            /**
             * Sets the value of the tokenSecret property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTokenSecret(String value) {
                this.tokenSecret = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="Code" use="required" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Status {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "Code", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String code;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the code property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCode() {
            return code;
        }

        /**
         * Sets the value of the code property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCode(String value) {
            this.code = value;
        }

    }

}
