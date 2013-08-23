package de.mvbonline.tools.restapidoc;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

/**
 * Creates a reStructuredText header with label.
 * 
 */
public class HeaderHelper implements Helper<Object> {

    public CharSequence apply(Object context, Options options) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(".. _");
        sb.append(context);
        sb.append("-rest:\n\n");
        sb.append(context);
        sb.append("\n");
        sb.append(StringUtils.repeat("-", context.toString().length()));
        return sb.toString();
    }

}
