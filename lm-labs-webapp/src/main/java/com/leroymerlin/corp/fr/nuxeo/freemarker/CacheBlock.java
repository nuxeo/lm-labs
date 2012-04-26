package com.leroymerlin.corp.fr.nuxeo.freemarker;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.cache.CacheWrapper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.cache.CacheableBean;
import com.leroymerlin.corp.fr.nuxeo.labs.site.cache.EhCacheWrapper;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class CacheBlock implements TemplateDirectiveModel {

    private static final String NAME_PARAM = "name";
    private static final String KEY_PARAM = "key";
    public static final String NO_CACHE_KEY = "NO_CACHE";

    @Override
    public void execute(Environment env,
            @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {

        String cacheName = getCacheNameFromParam(params, env);
        Serializable key = getCacheKeyFromParams(params, env);

        String result = "";
        if (key != null && !((String) key).contains(NO_CACHE_KEY)) {
            result = getBodyFromCache(cacheName, key, body);
        } else {
            result = renderBody(body);
        }

        Writer writer = env.getOut();
        writer.write(result);

    }


    private String getBodyFromCache(String cacheName, Serializable key,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        CacheManager cm = getCacheManager();
        CacheWrapper<Serializable, String> cw = new EhCacheWrapper<Serializable, String>(
                cacheName, cm);

        if (cw.get(key) == null) {
            cw.put(key, renderBody(body));
        }
        return cw.get(key);

    }


    private String renderBody(TemplateDirectiveBody body)
            throws TemplateException, IOException {
        StringWriter sw = new StringWriter();
        body.render(sw);
        return sw.toString();
    }

    @SuppressWarnings("rawtypes")
    private String getCacheNameFromParam(Map params, Environment env)
            throws TemplateException {
        checkForParam(params, NAME_PARAM, env);

        SimpleScalar scalar = (SimpleScalar) params.get(NAME_PARAM);
        if (scalar != null) {
            return scalar.getAsString();
        } else {
            return "default";
        }
    }

    @SuppressWarnings("rawtypes")
    private void checkForParam(Map params, String nameParam, Environment env)
            throws TemplateException {
        if (!params.containsKey(nameParam)) {
            throw new TemplateException("@cache directive must take a "
                    + nameParam + " parameter", env);
        }

    }

    @SuppressWarnings("rawtypes")
    private Serializable getCacheKeyFromParams(Map params, Environment env)
            throws TemplateException {

        checkForParam(params, KEY_PARAM, env);

        Object keyObj = params.get(KEY_PARAM);
        if (keyObj instanceof SimpleScalar) {
            return ((SimpleScalar) params.get(KEY_PARAM)).getAsString();
        } else if (keyObj instanceof StringModel) {
            if (((StringModel) keyObj).getWrappedObject() instanceof CacheableBean) {
                CacheableBean bean = (CacheableBean) ((StringModel) keyObj)
                        .getWrappedObject();
                return bean.getCacheKey();
            }
        }

        throw new TemplateException(
                "@cache key must implement CacheableBean interface or be a String",
                env);

    }

    private CacheManager getCacheManager() {
        try {
            return Framework.getService(CacheManager.class);
        } catch (Exception e) {
            return null;
        }
    }
}
