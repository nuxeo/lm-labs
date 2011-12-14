package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;


@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD)
public class LabsSiteWebAppUtilsTest {
    
    @Test
    public void iCanGetFoldersUnderFolder(){
        String directoryPath = FileUtils.getResourcePathFromContext("onpackage/testFoldersInFolder/");
        assertThat(LabsSiteWebAppUtils.getFoldersUnderFolder(directoryPath).size(), is(3)); 
    }

}
