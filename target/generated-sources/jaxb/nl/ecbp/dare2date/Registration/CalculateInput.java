//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.08 at 02:17:46 PM CET 
//


package nl.ecbp.dare2date.Registration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CalculateInput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CalculateInput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="paramlist" type="{http://www.han.nl/calculator}CalculateParameters"/>
 *         &lt;element name="operation" type="{http://www.han.nl/calculator}CalculateOperation"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CalculateInput", propOrder = {

})
public class CalculateInput {

    @XmlElement(required = true)
    protected CalculateParameters paramlist;
    @XmlElement(required = true)
    protected CalculateOperation operation;

    /**
     * Gets the value of the paramlist property.
     * 
     * @return
     *     possible object is
     *     {@link CalculateParameters }
     *     
     */
    public CalculateParameters getParamlist() {
        return paramlist;
    }

    /**
     * Sets the value of the paramlist property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalculateParameters }
     *     
     */
    public void setParamlist(CalculateParameters value) {
        this.paramlist = value;
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link CalculateOperation }
     *     
     */
    public CalculateOperation getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalculateOperation }
     *     
     */
    public void setOperation(CalculateOperation value) {
        this.operation = value;
    }

}
