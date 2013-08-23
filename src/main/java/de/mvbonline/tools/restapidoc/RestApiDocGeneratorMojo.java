package de.mvbonline.tools.restapidoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.DuplicateRealmException;
import org.reflections.Reflections;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.StringHelpers;

import de.mvbonline.tools.restapidoc.doclet.model.ClassDescription;
import de.mvbonline.tools.restapidoc.doclet.model.MethodDescription;
import de.mvbonline.tools.restapidoc.model.ApiBodyObjectDoc;
import de.mvbonline.tools.restapidoc.model.ApiDoc;
import de.mvbonline.tools.restapidoc.model.ApiMethodDoc;
import de.mvbonline.tools.restapidoc.model.ApiObjectDoc;
import de.mvbonline.tools.restapidoc.model.ApiParamDoc;
import de.mvbonline.tools.restapidoc.model.ApiResponseObjectDoc;

/**
 * Generates documentation of spring controllers in sphinx doc (rst Files) format.
 * 
 * @goal generatedocs
 * @requiresProject true
 * @requiresDependencyResolution compile
 */
public class RestApiDocGeneratorMojo extends AbstractMojo {
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter
     * @required
     */
    private String packageName;

    /**
     * @parameter
     */
    private String jacksonObjectMapper;

    /**
     * @parameter expression="${basedir}/src/site/sphinx"
     */
    private String sourceDocFolder;

    private Handlebars handlebars = new Handlebars();

    private Reflections reflections;

    private ApiDescriptionFinder apiDescriptionsFinder;

    private File outputtFolder;

    private Set<ApiObjectDoc> modelObjects = new HashSet<ApiObjectDoc>();

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            outputtFolder = new File(new File(sourceDocFolder), "restapi");
            outputtFolder.mkdirs();
            setupClasspath();
            setupHandlerbars();

            reflections = new Reflections(packageName);
            apiDescriptionsFinder = new ApiDescriptionFinder();
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controller : controllers) {
                getLog().info("Found Controller: " + controller.getName());
                processRestApiController(controller);
            }

            fillModelFields();

            generateFile(modelObjects, "api_models", "models");
        }
        catch (Exception e) {
            throw new MojoExecutionException("Failed", e);
        }
    }

    private void fillModelFields() {
        ObjectMapper objectMapper = null;
        if (!StringUtils.isBlank(jacksonObjectMapper)) {
            try {
                Class<?> mapperClass = Class.forName(jacksonObjectMapper, true, Thread.currentThread().getContextClassLoader());
                objectMapper = (ObjectMapper) mapperClass.newInstance();
            }
            catch (ClassNotFoundException e) {
                getLog().warn("Specified ObjectMapper not found " + jacksonObjectMapper);
            }
            catch (InstantiationException e) {
                getLog().warn("Specified ObjectMapper could not be created " + jacksonObjectMapper);
            }
            catch (IllegalAccessException e) {
                getLog().warn("Specified ObjectMapper could not be created " + jacksonObjectMapper);
            }
        }
        if(objectMapper == null) {
            objectMapper= new ObjectMapper();
            getLog().info("Using default ObjectMapper");
        }
        
        Jackson2ApiObjectDoc converter = new Jackson2ApiObjectDoc(objectMapper, apiDescriptionsFinder);
        for (ApiObjectDoc model : modelObjects) {
            try {
                converter.enrich(model);
            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void generateFile(Object doc, String templateName, String fileName) throws IOException {
        Template template = handlebars.compile(templateName);
        File f = new File(outputtFolder, fileName + ".rst");

        FileWriter fw = new FileWriter(f);
        fw.write(template.apply(doc));
        fw.close();
    }

    /**
     * build doc for all endpoint methods of the controller
     * 
     * @param classDescription
     *            the classDescription for the controller
     * @param controller
     *            the controller class
     * @return a List of all endpoint methods or an empty list if there are none
     */
    private List<ApiMethodDoc> getEndpointMethods(ClassDescription classDescription, Class<?> controller) {
        List<ApiMethodDoc> result = new LinkedList<ApiMethodDoc>();
        String basePath = "";

        if (controller.isAnnotationPresent(RequestMapping.class)) {
            basePath = controller.getAnnotation(RequestMapping.class).value()[0];
        }

        for (Method m : controller.getMethods()) {
            if (m.isAnnotationPresent(RequestMapping.class) && m.isAnnotationPresent(ResponseBody.class)) {
                RequestMapping rMappingAnno = m.getAnnotation(RequestMapping.class);
                ApiMethodDoc apiMethod = new ApiMethodDoc();
                apiMethod.setConsumes(Arrays.asList(rMappingAnno.consumes()));
                apiMethod.setProduces(Arrays.asList(rMappingAnno.produces()));
                apiMethod.setPath(basePath + rMappingAnno.value()[0]);
                apiMethod.setRequestMethod(rMappingAnno.method()[0]);

                MethodDescription methodDescription = apiDescriptionsFinder.getMethodDescription(classDescription, m.getName());
                apiMethod.setDescription(methodDescription != null ? methodDescription.getDescription() : "");

                Class<?>[] paramTypes = m.getParameterTypes();
                Annotation[][] paramAnnotations = m.getParameterAnnotations();
                ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
                String[] paramNames = parameterNameDiscoverer.getParameterNames(m);
                for (int i = 0; i < paramAnnotations.length; i++) {
                    for (Annotation annotation : paramAnnotations[i]) {
                        if (annotation instanceof RequestParam) {
                            Boolean required = ((RequestParam) annotation).required();
                            String name = ((RequestParam) annotation).value();
                            apiMethod.getQueryParameters().add(
                                    new ApiParamDoc(name, apiDescriptionsFinder.getElementDescription(methodDescription, paramNames[i]), paramTypes[i].getSimpleName(),
                                            required));
                        }
                        else if (annotation instanceof PathVariable) {
                            apiMethod.getUrlparameters().add(
                                    new ApiParamDoc(((PathVariable) annotation).value(), apiDescriptionsFinder.getElementDescription(methodDescription, paramNames[i]),
                                            paramTypes[i].getSimpleName(), true));
                        }
                        else if (annotation instanceof RequestBody) {
                            apiMethod.setBodyobject(new ApiBodyObjectDoc(paramTypes[i].getSimpleName()));

                            ClassDescription modelObjectDescription = apiDescriptionsFinder.getClassDescription(paramTypes[i].getName());
                            modelObjects.add(new ApiObjectDoc(paramTypes[i].getSimpleName(), modelObjectDescription != null ? modelObjectDescription
                                    .getDescription() : "",paramTypes[i].getName()));
                        }
                    }
                }

                ApiResponseObjectDoc apiResponseObjectDoc = new ApiResponseObjectDoc(m.getReturnType().getSimpleName());
                apiMethod.setResponse(apiResponseObjectDoc);
                result.add(apiMethod);
                ClassDescription modelObjectDescription = apiDescriptionsFinder.getClassDescription(m.getReturnType().getName());
                modelObjects.add(new ApiObjectDoc(m.getReturnType().getSimpleName(), modelObjectDescription != null ? modelObjectDescription
                        .getDescription() : "",m.getReturnType().getName()));
            }
        }

        return result;
    }




    /**
     * Process a single controller class
     * 
     * @param controller
     *            the controller
     * @throws IOException
     */
    private void processRestApiController(Class<?> controller) throws IOException {
        ApiDoc doc = new ApiDoc();
        doc.setName(controller.getSimpleName());

        ClassDescription classDescription = apiDescriptionsFinder.getClassDescription(controller.getName());
        doc.setDescription(classDescription != null ? classDescription.getDescription() : "");

        doc.setMethods(getEndpointMethods(classDescription, controller));

        if (doc.getMethods().isEmpty()) {
            getLog().info("No RESTful methods found for controller " + controller.getName());
            return;
        }
        generateFile(doc, "apidoc", doc.getName());
    }

    private void setupClasspath() throws DependencyResolutionRequiredException, DuplicateRealmException, MalformedURLException {
        List<String> elements = project.getCompileClasspathElements();
        ClassWorld world = new ClassWorld();
        ClassRealm realm;
        realm = world.newRealm("maven.plugin." + getClass().getSimpleName(), Thread.currentThread().getContextClassLoader());
        for (String e : elements) {
            File elementFile = new File(e);
            URL url = new URL("file:///" + elementFile.getPath() + (elementFile.isDirectory() ? "/" : ""));
            realm.addConstituent(url);
        }

        Thread.currentThread().setContextClassLoader(realm.getClassLoader());
    }

    private void setupHandlerbars() {
        handlebars.registerHelper(StringHelpers.lower.name(), StringHelpers.lower);
        handlebars.registerHelper("header", new HeaderHelper());
        handlebars.prettyPrint(true);
    }

}
