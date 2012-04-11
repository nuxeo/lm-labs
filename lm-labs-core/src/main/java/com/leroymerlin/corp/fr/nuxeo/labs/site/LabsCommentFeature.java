package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Deploy( { "org.nuxeo.ecm.relations.api", "org.nuxeo.ecm.relations",
    "org.nuxeo.ecm.relations.core.listener",
    "org.nuxeo.ecm.relations.jena",
    "com.leroymerlin.labs.core:OSGI-INF/test-commentService-config-bundle.xml",
    "com.leroymerlin.labs.core:OSGI-INF/test-relation-config-bundle.xml",
    "org.nuxeo.ecm.platform.comment.api",
    "org.nuxeo.ecm.platform.comment.core",
    "org.nuxeo.ecm.platform.comment:OSGI-INF/comment-listener-contrib.xml",
    "org.nuxeo.ecm.platform.comment:OSGI-INF/CommentService.xml"})
@Features(PlatformFeature.class)
public class LabsCommentFeature extends SimpleFeature {
}
