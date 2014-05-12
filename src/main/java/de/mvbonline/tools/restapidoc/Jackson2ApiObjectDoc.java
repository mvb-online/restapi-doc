package de.mvbonline.tools.restapidoc;

import static com.google.common.collect.Maps.uniqueIndex;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Function;

import de.mvbonline.tools.restapidoc.doclet.model.ClassDescription;
import de.mvbonline.tools.restapidoc.model.ApiObjectDoc;
import de.mvbonline.tools.restapidoc.model.ApiObjectFieldDoc;

public class Jackson2ApiObjectDoc {

    private ObjectMapper objectMapper;
    private ApiDescriptionFinder apiDescriptionFinder;

    public Jackson2ApiObjectDoc(ObjectMapper objectMapper, ApiDescriptionFinder apiDescriptionFinder) {
        super();
        this.objectMapper = objectMapper;
        this.apiDescriptionFinder = apiDescriptionFinder;
    }

    private Function<BeanPropertyDefinition, String> beanPropertyByInternalName() {
        return new Function<BeanPropertyDefinition, String>() {
            public String apply(BeanPropertyDefinition input) {
                return input.getInternalName();
            }
        };
    }

    public void enrich(ApiObjectDoc model) throws ClassNotFoundException {
        if (!model.isPrimitiv()) {
            TypeResolver typeResolver = new TypeResolver();
            ResolvedType resolvedType = typeResolver.resolve(Class.forName(model.getModelClass(), true, Thread.currentThread()
                    .getContextClassLoader()));
            SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
            BeanDescription beanDescription = serializationConfig.introspect(TypeFactory.defaultInstance()
                    .constructType(resolvedType.getErasedType()));

            MemberResolver memberResolver = new MemberResolver(typeResolver);
            ResolvedTypeWithMembers resolvedMemberWithMembers = memberResolver.resolve(resolvedType, null, null);
            Map<String, BeanPropertyDefinition> propertyLookup = uniqueIndex(beanDescription.findProperties(), beanPropertyByInternalName());

            ClassDescription classDescription = apiDescriptionFinder.getClassDescription(model.getModelClass());
            for (ResolvedField childField : resolvedMemberWithMembers.getMemberFields()) {
                if (propertyLookup.containsKey(childField.getName())) {
                    BeanPropertyDefinition propertyDefinition = propertyLookup.get(childField.getName());
                    AnnotatedMember member = propertyDefinition.getField();
                    if (member != null && member.getMember() != null && Field.class.isAssignableFrom(member.getMember().getClass())) {
                        ApiObjectFieldDoc apiField = new ApiObjectFieldDoc();

                        apiField.setName(propertyDefinition.getName());
                        apiField.setType(propertyDefinition.getField().getRawType().getSimpleName());
                        apiField.setDescription(apiDescriptionFinder.getFieldDescription(classDescription, propertyDefinition.getName()));
                        apiField.setAnnotations(apiDescriptionFinder.getFieldAnnotations(classDescription, propertyDefinition.getName()));

                        if (Collection.class.isAssignableFrom(propertyDefinition.getField().getRawType())) {
                            apiField.setMultiple(true);
                            if (propertyDefinition.getField().getGenericType() instanceof ParameterizedType) {
                                Class<?> c = (Class<?>) ((ParameterizedType) propertyDefinition.getField().getGenericType()).getActualTypeArguments()[0];
                                apiField.setType(c.getSimpleName());
                            }
                        }
                        model.addField(apiField);
                    }
                }
            }
        }
    }
}
