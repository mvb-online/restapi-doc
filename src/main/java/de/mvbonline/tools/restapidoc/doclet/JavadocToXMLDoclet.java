package de.mvbonline.tools.restapidoc.doclet;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

import de.mvbonline.tools.restapidoc.doclet.model.ApiDescription;
import de.mvbonline.tools.restapidoc.doclet.model.ClassDescription;
import de.mvbonline.tools.restapidoc.doclet.model.ElementDescription;
import de.mvbonline.tools.restapidoc.doclet.model.MethodDescription;

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
                doc.addProperty(new ElementDescription(f.name(), f.commentText()));
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
