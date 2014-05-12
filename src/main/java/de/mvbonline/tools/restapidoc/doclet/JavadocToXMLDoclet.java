package de.mvbonline.tools.restapidoc.doclet;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sun.javadoc.*;

import de.mvbonline.tools.restapidoc.doclet.model.*;

/**
 * Doclet for collection all Javadoc comments and storing them into an xml file for runtime access.
 *
 */
public class JavadocToXMLDoclet {
    public static final String FILENAME_APIDOC_XML = "apidoc.xml";

    public static boolean start(RootDoc root) throws Exception {
        ApiDescription apidoc = new ApiDescription();
        for (ClassDoc c : root.classes()) {
            ClassDescription doc = new ClassDescription();
            doc.setFullClassName(c.qualifiedName());
            doc.setName(c.name());
            doc.setDescription(c.commentText());

            for (FieldDoc f : c.fields(false)) {
                ElementDescription ed = new ElementDescription(f.name(), f.commentText());

                if (f.annotations() != null && f.annotations().length > 0) {
                    List<Annotation> annotationList = new ArrayList<Annotation>();

                    for (AnnotationDesc ad : f.annotations()) {
                        if (ad != null && ad.elementValues() != null) {
                            List<AnnotationKeyValue> keyValues = new ArrayList<AnnotationKeyValue>();

                            for (AnnotationDesc.ElementValuePair valuePair : ad.elementValues()) {
                                AnnotationKeyValue keyValue = new AnnotationKeyValue(valuePair.element().name(),
                                    valuePair.value().value().toString());
                                keyValues.add(keyValue);
                            }

                            Annotation annotation = new Annotation(ad.annotationType().name());
                            annotation.setKeyValues(keyValues);
                            annotationList.add(annotation);
                        }
                    }

                    ed.setAnnotations(annotationList);
                }

                doc.addProperty(ed);
            }

            for (MethodDoc m : c.methods(false)) {
                MethodDescription mdoc = new MethodDescription();
                mdoc.setName(m.name());
                mdoc.setDescription(m.commentText());
                if (m.commentText() != null && m.commentText().length() > 0) {
                    for (ParamTag p : m.paramTags()) {
                        mdoc.addParameter(new ElementDescription(p.parameterName(), p.parameterComment()));
                    }
                        
                    for (Tag t : m.tags("return")) {
                        if (t.text() != null && t.text().length() > 0)
                            mdoc.setReturnValue(new ElementDescription("", t.text()));
                    }
                }
                doc.addMethod(mdoc);
            }
            apidoc.addClass(doc);
        }
        
        final JAXBContext jaxbContext = JAXBContext.newInstance(ApiDescription.class);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(apidoc, new File(FILENAME_APIDOC_XML));

        return true;
    }
}
