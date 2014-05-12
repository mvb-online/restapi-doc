package de.mvbonline.tools.restapidoc;

import de.mvbonline.tools.restapidoc.doclet.model.ApiDescription;
import de.mvbonline.tools.restapidoc.model.Blacklist;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;

import javax.xml.bind.JAXB;
import java.io.IOException;

/**
 * @author Markus M. May <m.may@mvb-online.de>
 */
public class BlacklistLoader {

    private Blacklist blacklist = new Blacklist();

    private static BlacklistLoader loader = new BlacklistLoader();

    private BlacklistLoader() {
        super();
        try {
            loadBlacklist();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BlacklistLoader getInstance() {
        return loader;
    }

    private void loadBlacklist() throws IOException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        Resource[] blacklistsRes = applicationContext.getResources("classpath*:/blacklist.xml");
        blacklist = new Blacklist();
        for (Resource blacklistRes : blacklistsRes) {
            Blacklist tmpBlacklist = JAXB.unmarshal(blacklistRes.getInputStream(), Blacklist.class);

            blacklist.getAnnotationsOnBlacklist().addAll(tmpBlacklist.getAnnotationsOnBlacklist());
            blacklist.getClassesOnBlacklist().addAll(tmpBlacklist.getClassesOnBlacklist());
            blacklist.getMethodsOnBlacklist().addAll(tmpBlacklist.getMethodsOnBlacklist());
            blacklist.getFieldsOnBlacklist().addAll(tmpBlacklist.getFieldsOnBlacklist());
        }
    }

    public boolean methodInBlacklist(String methodName) {
        return this.blacklist.getMethodsOnBlacklist().contains(methodName);
    }

    public boolean fieldInBlacklist(String fieldName) {
        return this.blacklist.getFieldsOnBlacklist().contains(fieldName);
    }

    public boolean classInBlacklist(String className) {
        return this.blacklist.getClassesOnBlacklist().contains(className);
    }

    public boolean annotationInBlacklist(String annotationName) {
        return this.blacklist.getAnnotationsOnBlacklist().contains(annotationName);
    }
}
