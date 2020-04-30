//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.22 at 10:11:00 PM EST 
//


package net.sf.cb2xml.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
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
 *         &lt;element ref="{}condition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}item" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="assumed-digits" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="depending-on" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="display-length" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="editted-numeric" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="inherited-usage" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="insert-decimal-point" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="justified" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="level" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="numeric" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="occurs" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="occurs-min" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="picture" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="position" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="redefined" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="redefines" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="scale" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="sign-position" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="sign-separate" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="signed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="storage-length" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="sync" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="usage" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "condition",
    "item"
})
@XmlRootElement(name = "item")
public class Item {

    protected List<Condition> condition;
    protected List<Item> item;
    @XmlAttribute(name = "assumed-digits")
    protected Integer assumedDigits;
    @XmlAttribute(name = "depending-on")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String dependingOn;
    @XmlAttribute(name = "display-length", required = true)
    protected int displayLength;
    @XmlAttribute(name = "editted-numeric")
    protected Boolean edittedNumeric;
    @XmlAttribute(name = "inherited-usage")
    protected Boolean inheritedUsage;
    @XmlAttribute(name = "insert-decimal-point")
    protected Boolean insertDecimalPoint;
    @XmlAttribute(name = "justified")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String justified;
    @XmlAttribute(name = "level", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String level;
    @XmlAttribute(name = "name", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute(name = "numeric")
    protected Boolean numeric;
    @XmlAttribute(name = "occurs")
    protected Integer occurs;
    @XmlAttribute(name = "occurs-min")
    protected Integer occursMin;
    @XmlAttribute(name = "picture")
    @XmlSchemaType(name = "anySimpleType")
    protected String picture;
    @XmlAttribute(name = "position", required = true)
    protected int position;
    @XmlAttribute(name = "redefined")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String redefined;
    @XmlAttribute(name = "redefines")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String redefines;
    @XmlAttribute(name = "scale")
    protected Integer scale;
    @XmlAttribute(name = "sign-position")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String signPosition;
    @XmlAttribute(name = "sign-separate")
    protected Boolean signSeparate;
    @XmlAttribute(name = "signed")
    protected Boolean signed;
    @XmlAttribute(name = "storage-length", required = true)
    protected int storageLength;
    @XmlAttribute(name = "sync")
    protected Boolean sync;
    @XmlAttribute(name = "usage")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String usage;
    @XmlAttribute(name = "value")
    @XmlSchemaType(name = "anySimpleType")
    protected String value;

    /**
     * Gets the value of the condition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the condition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCondition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Condition }
     * 
     * 
     */
    public List<Condition> getCondition() {
        if (condition == null) {
            condition = new ArrayList<Condition>();
        }
        return this.condition;
    }

    /**
     * Gets the value of the item property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the item property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Item }
     * 
     * 
     */
    public List<Item> getItem() {
        if (item == null) {
            item = new ArrayList<Item>();
        }
        return this.item;
    }

    /**
     * Gets the value of the assumedDigits property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAssumedDigits() {
        return assumedDigits;
    }

    /**
     * Sets the value of the assumedDigits property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAssumedDigits(Integer value) {
        this.assumedDigits = value;
    }

    /**
     * Gets the value of the dependingOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependingOn() {
        return dependingOn;
    }

    /**
     * Sets the value of the dependingOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependingOn(String value) {
        this.dependingOn = value;
    }

    /**
     * Gets the value of the displayLength property.
     * 
     */
    public int getDisplayLength() {
        return displayLength;
    }

    /**
     * Sets the value of the displayLength property.
     * 
     */
    public void setDisplayLength(int value) {
        this.displayLength = value;
    }

    /**
     * Gets the value of the edittedNumeric property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEdittedNumeric() {
        return edittedNumeric;
    }

    /**
     * Sets the value of the edittedNumeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEdittedNumeric(Boolean value) {
        this.edittedNumeric = value;
    }

    /**
     * Gets the value of the inheritedUsage property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInheritedUsage() {
        return inheritedUsage;
    }

    /**
     * Sets the value of the inheritedUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInheritedUsage(Boolean value) {
        this.inheritedUsage = value;
    }

    /**
     * Gets the value of the insertDecimalPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInsertDecimalPoint() {
        return insertDecimalPoint;
    }

    /**
     * Sets the value of the insertDecimalPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInsertDecimalPoint(Boolean value) {
        this.insertDecimalPoint = value;
    }

    /**
     * Gets the value of the justified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustified() {
        return justified;
    }

    /**
     * Sets the value of the justified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustified(String value) {
        this.justified = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel(String value) {
        this.level = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the numeric property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNumeric() {
        return numeric;
    }

    /**
     * Sets the value of the numeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNumeric(Boolean value) {
        this.numeric = value;
    }

    /**
     * Gets the value of the occurs property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOccurs() {
        return occurs;
    }

    /**
     * Sets the value of the occurs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOccurs(Integer value) {
        this.occurs = value;
    }

    /**
     * Gets the value of the occursMin property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOccursMin() {
        return occursMin;
    }

    /**
     * Sets the value of the occursMin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOccursMin(Integer value) {
        this.occursMin = value;
    }

    /**
     * Gets the value of the picture property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets the value of the picture property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPicture(String value) {
        this.picture = value;
    }

    /**
     * Gets the value of the position property.
     * 
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     */
    public void setPosition(int value) {
        this.position = value;
    }

    /**
     * Gets the value of the redefined property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedefined() {
        return redefined;
    }

    /**
     * Sets the value of the redefined property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedefined(String value) {
        this.redefined = value;
    }

    /**
     * Gets the value of the redefines property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedefines() {
        return redefines;
    }

    /**
     * Sets the value of the redefines property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedefines(String value) {
        this.redefines = value;
    }

    /**
     * Gets the value of the scale property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * Sets the value of the scale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setScale(Integer value) {
        this.scale = value;
    }

    /**
     * Gets the value of the signPosition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignPosition() {
        return signPosition;
    }

    /**
     * Sets the value of the signPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignPosition(String value) {
        this.signPosition = value;
    }

    /**
     * Gets the value of the signSeparate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSignSeparate() {
        return signSeparate;
    }

    /**
     * Sets the value of the signSeparate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSignSeparate(Boolean value) {
        this.signSeparate = value;
    }

    /**
     * Gets the value of the signed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSigned() {
        return signed;
    }

    /**
     * Sets the value of the signed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSigned(Boolean value) {
        this.signed = value;
    }

    /**
     * Gets the value of the storageLength property.
     * 
     */
    public int getStorageLength() {
        return storageLength;
    }

    /**
     * Sets the value of the storageLength property.
     * 
     */
    public void setStorageLength(int value) {
        this.storageLength = value;
    }

    /**
     * Gets the value of the sync property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSync() {
        return sync;
    }

    /**
     * Sets the value of the sync property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSync(Boolean value) {
        this.sync = value;
    }

    /**
     * Gets the value of the usage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets the value of the usage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsage(String value) {
        this.usage = value;
    }

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

}
