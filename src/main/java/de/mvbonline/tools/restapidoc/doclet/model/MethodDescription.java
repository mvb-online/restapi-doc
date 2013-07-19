package de.mvbonline.tools.restapidoc.doclet.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Model for javadoc comments on a method.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class MethodDescription extends ElementDescription implements Serializable {

    private ElementDescription returnValue;

    @XmlElement
    private List<ElementDescription> parameters;

    public void addParameter(ElementDescription doc) {
        if (this.parameters == null) {
            this.parameters = new LinkedList<ElementDescription>();
        }
        this.parameters.add(doc);
    }

    public List<ElementDescription> getParameters() {
        return parameters;
    }

    public ElementDescription getReturnValue() {
        return returnValue;
    }

    public void setParameters(List<ElementDescription> parameters) {
        this.parameters = parameters;
    }

    public void setReturnValue(ElementDescription returnValue) {
        this.returnValue = returnValue;
    }
}
